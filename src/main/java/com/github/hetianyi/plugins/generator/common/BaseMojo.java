package com.github.hetianyi.plugins.generator.common;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import cn.hutool.db.Db;
import com.github.hetianyi.boot.ready.common.Const;
import com.github.hetianyi.boot.ready.common.util.CollectionUtil;
import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * @author Jason He
 */
@Slf4j
public abstract class BaseMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    public MavenProject project;
    @Parameter
    private DbConfigurationProperties database;
    @Parameter
    private List<String> activeProfiles;
    @Parameter
    private List<ProfileProperties> profiles;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        configureDefaultValue();

        printConfig();

        if (CollectionUtil.isNullOrEmpty(profiles)) {
            log.info("没有要执行的profile");
        }

        InstanceConfig.setProfileProperties(profiles);
        InstanceConfig.setActiveProfiles(activeProfiles);
        InstanceConfig.setDbProperties(database);

        ClassLoader classLoader = getClassLoader(project);
        InstanceConfig.setClassLoader(classLoader);

        // 连接数据库
        if ("mysql".equalsIgnoreCase(database.getType())) {
            connectMysqlDB();
        }
        else if ("postgresql".equalsIgnoreCase(database.getType())) {
            connectPostgresqlDB();
        }
        else {
            throw new RuntimeException("不支持的数据库类型: " + database.getType());
        }

        // 执行激活的profile
        try {
            for (ProfileProperties props : this.profiles) {
                if (activeProfiles.stream().filter(v -> Objects.equals(v, props.getName())).findAny().isPresent()) {
                    runProfile(props);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new MojoFailureException(e);
        }
    }

    private void printConfig() {
        log.info("开始执行插件");
        log.debug("dbConfig=" + Const.GSON.toJson(database));
        log.debug("activeProfiles=" + Const.GSON.toJson(activeProfiles));
        log.debug("profiles=" + Const.GSON.toJson(profiles));
        log.debug("当前目录=" + project.getBasedir());
    }

    private void connectMysqlDB() throws MojoFailureException {
        try {
            MysqlDataSource ds = new MysqlDataSource();
            ds.setServerName(database.getHost());
            ds.setPort(database.getPort());
            ds.setUser(database.getUsername());
            ds.setPassword(database.getPassword());
            ds.setCharacterEncoding("utf8");
            ds.setServerTimezone("GMT+8");
            ds.setAutoReconnect(true);
            ds.setDatabaseName(database.getDbName());

            log.info("连接MySQL数据库: " + ds.getUrl());

            Db dbUtil = Db.use(ds, "com.mysql.cj.jdbc.Driver");
            InstanceConfig.setDb(dbUtil);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new MojoFailureException(e);
        }
    }

    private void connectPostgresqlDB() throws MojoFailureException {
        try {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setServerName(database.getHost());
            ds.setDatabaseName(database.getDbName());
            ds.setUser(database.getUsername());
            ds.setPassword(database.getPassword());
            ds.setCurrentSchema(database.getSchema());

            log.info("连接PG数据库: " + ds.getUrl());

            Db dbUtil = Db.use(ds, "org.postgresql.Driver");
            InstanceConfig.setDb(dbUtil);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new MojoFailureException(e);
        }
    }

    protected void runProfile(ProfileProperties profile) throws Exception {
        getLog().info("-----------------------------[ "
                              + "profile: "
                              + profile.getName() +
                              " ]----------------------------");
    }

    private void configureDefaultValue() {
        if (null == database) {
            database = new DbConfigurationProperties();
        }
        if (null == activeProfiles) {
            activeProfiles = new LinkedList<>();
        }
        if (null == profiles) {
            activeProfiles = new LinkedList<>();
        }
    }

    private ClassLoader getClassLoader(MavenProject project) {
        try {
            // 所有的类路径环境，也可以直接用 compilePath
            List classpathElements = project.getCompileClasspathElements();

            classpathElements.add(project.getBuild().getOutputDirectory());
            classpathElements.add(project.getBuild().getTestOutputDirectory());
            // 转为 URL 数组
            URL urls[] = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File((String) classpathElements.get(i)).toURL();
            }
            // 自定义类加载器
            return new URLClassLoader(urls, this.getClass().getClassLoader());
        }
        catch (Exception e) {
            log.debug("Couldn't get the classloader.");
            return this.getClass().getClassLoader();
        }
    }
}
