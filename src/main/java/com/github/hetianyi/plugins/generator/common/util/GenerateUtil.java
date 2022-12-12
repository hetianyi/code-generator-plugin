package com.github.hetianyi.plugins.generator.common.util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.github.hetianyi.boot.ready.common.Const;
import com.github.hetianyi.boot.ready.common.util.CollectionUtil;
import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.common.DbConfigurationProperties;
import com.github.hetianyi.plugins.generator.common.DbType;
import com.github.hetianyi.plugins.generator.common.InstanceConfig;
import com.github.hetianyi.plugins.generator.common.ProfileProperties;
import com.github.hetianyi.plugins.generator.common.SQL;
import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jason He
 */
@Slf4j
public class GenerateUtil {

    public static final ThreadLocal<ProfileProperties> currentProfile = new ThreadLocal<>();

    private static final ThreadLocal<DbType> databaseType = new ThreadLocal<>();

    /**
     * 获取表信息
     */
    public static Map<String, ClassGenerator> resolveTables(ProfileProperties profile) throws Exception {

        currentProfile.set(profile);

        if ("postgresql".equalsIgnoreCase(InstanceConfig.getDbProperties().getType())) {
            databaseType.set(DbType.POSTGRESQL);
        }
        else {
            databaseType.set(DbType.MYSQL);
        }
        Db db = InstanceConfig.getDb();

        DbConfigurationProperties dbProperties = InstanceConfig.getDbProperties();

        List<Entity> query;
        if (databaseType.get() == DbType.POSTGRESQL) {
            query = db.query(SQL.QUERY_PG_TABLES, dbProperties.getSchema());
        }
        else {
            query = db.query(SQL.QUERY_MYSQL_TABLES, dbProperties.getDbName());
        }

        List<TableDefinition> tabDefs = new LinkedList<>();

        query.forEach(v -> {
            if (!CollectionUtil.isNullOrEmpty(profile.getExcludeTables())
                    && profile.getExcludeTables().contains((String) v.get("TABLE_NAME"))) {
                return;
            }
            if (!CollectionUtil.isNullOrEmpty(profile.getIncludeTables())
                    && profile.getIncludeTables().contains((String) v.get("TABLE_NAME"))) {
                tabDefs.add(TableDefinition.builder()
                                           .name((String) v.get("TABLE_NAME"))
                                           .type((String) v.get("TABLE_TYPE"))
                                           .comment((String) v.get("TABLE_COMMENT"))
                                           .build());
            }
            else {
                tabDefs.add(TableDefinition.builder()
                                           .name((String) v.get("TABLE_NAME"))
                                           .type((String) v.get("TABLE_TYPE"))
                                           .comment((String) v.get("TABLE_COMMENT"))
                                           .build());
            }
        });

        if (CollectionUtil.isNullOrEmpty(tabDefs)) {
            log.info("没有需要导出的表");
            return ImmutableMap.of();
        }

        //System.out.println(Const.GSON.toJson(tabDefs));

        Map<String, ClassGenerator> resultMap = new HashMap<>();
        for (TableDefinition tabDef : tabDefs) {
            GenerateUtil.getTableColumnDef(tabDef);
            ClassGenerator generator = GenerateUtil.getClassGenerator(tabDef);
            if (resultMap.containsKey(generator.getClassName())) {
                throw new RuntimeException("类名重复: " + generator.getClassName());
            }
            resultMap.put(generator.getClassName(), generator);
        }
        //System.out.println(Const.GSON.toJson(tabDefs));
        if (CollectionUtil.isNullOrEmpty(currentProfile.get().getTemplateDirs())) {
            createPackageDir();
        }
        return resultMap;
    }


    /**
     * 获取表的列信息
     */
    public static void getTableColumnDef(TableDefinition td) throws Exception {
        DbConfigurationProperties dbProperties = InstanceConfig.getDbProperties();
        Db db = InstanceConfig.getDb();

        List<Entity> result;
        if (databaseType.get() == DbType.POSTGRESQL) {
            result = db.query(SQL.QUERY_PG_TABLE_SCHEMA,
                              td.getName(),
                              td.getName(),
                              dbProperties.getSchema(),
                              td.getName());
        }
        else {
            result = db.query(SQL.QUERY_MYSQL_TABLE_SCHEMA, dbProperties.getDbName(), td.getName());
        }
        //System.out.println("column result = \n" + Const.GSON.toJson(result));

        List<TableColumn> fields = new LinkedList<>();
        result.forEach(c -> {
            String extra = Optional.ofNullable((String) c.get("EXTRA"))
                                   .orElse("");
            fields.add(TableColumn.builder()
                                  .position(Integer.valueOf(c.get("ORDINAL_POSITION").toString()))
                                  .name((String) c.get("COLUMN_NAME"))
                                  .type((String) c.get("DATA_TYPE"))
                                  .comment((String) c.get("COLUMN_COMMENT"))
                                  .id(Optional.ofNullable((String) c.get("COLUMN_KEY")).orElse("").contains("PRI"))
                                  .autoIncrement(extra.contains("auto_increment") || extra.contains("nextval("))
                                  .build());
        });

        td.setColumns(fields);
    }


    public static ClassGenerator getClassGenerator(TableDefinition tabDef) {
        return ClassGenerator.from(tabDef, currentProfile.get()).generateStructure();
    }


    public static String removePrefixOrSuffix(String name) {
        name = name.toLowerCase();
        Set<String> ignorePrefix = currentProfile.get().getIgnorePrefixes();
        Set<String> ignoreSuffix = currentProfile.get().getIgnoreSuffixes();
        if (!CollectionUtil.isNullOrEmpty(ignorePrefix)) {
            for (String prefix : ignorePrefix) {
                if (StringUtil.isNullOrEmptyTrimed(prefix)) {
                    continue;
                }
                if (name.startsWith(prefix.toLowerCase())) {
                    name = name.substring(prefix.length());
                }
            }
        }
        if (!CollectionUtil.isNullOrEmpty(ignoreSuffix)) {
            for (String suffix : ignoreSuffix) {
                if (StringUtil.isNullOrEmptyTrimed(suffix)) {
                    continue;
                }
                if (name.endsWith(suffix.toLowerCase())) {
                    name = name.substring(0, name.length() - suffix.length());
                }
            }
        }
        return name;
    }

    public static String camel(String name, boolean isTable) {
        name = name.toLowerCase();
        Pattern pattern = Pattern.compile("[-_]+([^_^-])");
        Matcher matcher = pattern.matcher(name);

        StringBuilder sb = new StringBuilder();

        int lastEnd = 0;

        while (matcher.find()) {
            String validContent = matcher.group(1);
            int start = matcher.start();
            int end = matcher.end();

            sb.append(name, lastEnd, start);
            sb.append(validContent.toUpperCase());
            lastEnd = end;
        }
        sb.append(name.substring(lastEnd));

        String ret = sb.toString();

        return (isTable ? ret.substring(0, 1).toUpperCase() : ret.substring(0, 1).toLowerCase()) + ret.substring(1);
    }

    public static void createPackageDir(String packageName) throws IOException {
        String[] paths = packageName.split("\\.");
        Path pkgDir = Paths.get(currentProfile.get().getOutputDir(), paths);
        log.info("创建程序包输出目录: {}", pkgDir);
        Files.createDirectories(pkgDir);
    }

    public static void createPackageDir() throws IOException {
        String packageName = currentProfile.get().getPackageName();
        String[] paths = packageName.split("\\.");
        Path pkgDir = Paths.get(currentProfile.get().getOutputDir(), paths);
        log.info("创建程序包输出目录: {}", pkgDir);
        Files.createDirectories(pkgDir);
    }


    /*public static void main(String[] args) {
        System.out.println(camel("t_sex__--age__---_name", true));
        System.out.println(camel("t_sex__--age__---_name", false));
    }*/
}
