package com.github.hetianyi.plugins.generator.mvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import cn.hutool.core.io.FileUtil;
import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.common.BaseMojo;
import com.github.hetianyi.plugins.generator.common.ProfileProperties;
import com.github.hetianyi.plugins.generator.common.TemplateConfig;
import com.github.hetianyi.plugins.generator.common.TemplateType;
import com.github.hetianyi.plugins.generator.common.util.GenerateUtil;
import com.github.hetianyi.plugins.generator.common.util.InflectorUtil;
import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * 根据Controller, Service, ServiceImpl, Mapper, Mapper.xml等模版代码
 * 生成数据库表对应的增删改查接口。
 *
 * @author Jason
 */
@Slf4j
@Mojo(name = "generate-mvc", defaultPhase = LifecyclePhase.COMPILE)
public class MvcMojo extends BaseMojo {


    protected void runProfile(ProfileProperties profile) throws Exception {

        super.runProfile(profile);

        if (StringUtil.isNullOrEmpty(profile.getMvcTemplateDir())) {
            getLog().info("未配置mvcTemplateDir, profile: " + profile.getName() + "将终止执行");
            return;
        }

        Map<String, ClassGenerator> map = GenerateUtil.resolveTables(profile);

        List<File> templateTypeFiles = detectTemplateFile(profile);

        for (Map.Entry<String, ClassGenerator> entry : map.entrySet()) {
            findReplaceCopy(templateTypeFiles, entry);
        }
    }


    private List<File> detectTemplateFile(ProfileProperties profile) throws IOException {

        List<File> result = new LinkedList<>();
        String mvcTemplateDir = profile.getMvcTemplateDir();
        Path templateRootPath = Paths.get(mvcTemplateDir);

        if (!Files.exists(templateRootPath)) {
            throw new RuntimeException("模版目录不存在: " + templateRootPath);
        }
        log.debug("遍历目录：" + templateRootPath);
        Files.walk(templateRootPath).forEach(v -> {
            File file = v.toFile();
            if (file.isDirectory()) {
            }
            else {
                // System.out.println("文件 -> " + v.toFile().getAbsolutePath());
                String content = FileUtil.readUtf8String(file);
                // System.out.println("文件内容 -> " + content);
                for (Map.Entry<TemplateType, String> entry : TemplateConfig.templateCommentMarker.entrySet()) {
                    if (content.contains(entry.getValue())) {
                        result.add(file);
                        log.debug("模版文件 -> " + v.toFile().getAbsolutePath());
                    }
                }
            }
        });

        return result;
    }

    private void findReplaceCopy(List<File> templateTypeFiles, Map.Entry<String, ClassGenerator> entry) {

        String pojoClassName = entry.getKey();

        log.info("创建模版复制: " + pojoClassName);

        ClassGenerator generator = entry.getValue();

        String lowerObjectName = generator.getLowerObjectName();
        String upperObjectName = generator.getUpperObjectName();
        String entityName = Optional.ofNullable(generator.getTabDef().getComment())
                                    .orElse("")
                                    .replace("\r\n", "\n")
                                    .replace("\n", " ")
                                    .replace("表", "");


        for (File e : templateTypeFiles) {

            File templateFileDir = e.getParentFile();
            String templateFileName = e.getName();
            String copyFileName = templateFileName.replace("Example", upperObjectName);
            String templateContent = FileUtil.readUtf8String(e);

            StringJoiner sJoiner = new StringJoiner("`, `");
            generator.getTabDef().getColumns().forEach(v -> sJoiner.add(v.getName()));
            TableColumn idColumn = generator.getTabDef()
                                            .getColumns()
                                            .stream()
                                            .filter(v -> v.isId())
                                            .findFirst()
                                            .orElse(null);

            String allFields = "`" + sJoiner + "`";

            templateContent = templateContent.replace("// " + TemplateConfig.MVC_MARKER + "\r\n", "")
                                             .replace("// " + TemplateConfig.MVC_MARKER + "\n", "")
                                             .replace("<!--" + TemplateConfig.MVC_MARKER + "-->\r\n", "")
                                             .replace("<!--" + TemplateConfig.MVC_MARKER + "-->\n", "")
                                             .replace(TemplateConfig.MVC_MARKER + "\r\n", "")
                                             .replace(TemplateConfig.MVC_MARKER + "\n", "")
                                             .replace(TemplateConfig.MVC_MARKER, "")
                                             .replaceAll(TemplateConfig.MVC_MAPPER_ALL_FIELDS_MARKER, allFields)
                                             .replaceAll(TemplateConfig.MVC_MAPPER_ID_FIELD_MARKER,
                                                         null == idColumn ? "`id`" : "`" + idColumn.getName() + "`");

            String tempTabName = UUID.randomUUID().toString();
            templateContent = templateContent.replace("t_example", tempTabName)
                                             .replace("examples",
                                                      InflectorUtil.getInstance().pluralize(lowerObjectName))
                                             .replace("Examples",
                                                      InflectorUtil.getInstance().pluralize(upperObjectName))
                                             .replace("Example", upperObjectName)
                                             .replace("example", lowerObjectName)
                                             .replace(tempTabName, generator.getTabDef().getName())
                                             .replace("示例", entityName);

            File targetFile = new File(templateFileDir, copyFileName);
            FileUtil.writeUtf8String(templateContent, targetFile);
        }
    }
}