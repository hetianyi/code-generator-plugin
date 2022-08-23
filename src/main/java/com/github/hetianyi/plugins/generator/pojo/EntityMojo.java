package com.github.hetianyi.plugins.generator.pojo;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.common.BaseMojo;
import com.github.hetianyi.plugins.generator.common.ProfileProperties;
import com.github.hetianyi.plugins.generator.common.util.GenerateUtil;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * 根据数据库表schema生成Java实体类。
 * 支持MySQL和Postgresql数据库
 *
 * @author Jason
 */
@Mojo(name = "generate-pojo", defaultPhase = LifecyclePhase.COMPILE)
public class EntityMojo extends BaseMojo {

    protected void runProfile(ProfileProperties profile) throws Exception {

        super.runProfile(profile);

        if (!StringUtil.isNullOrEmpty(profile.getMvcTemplateDir())) {
            getLog().info("已配置mvcTemplateDir, mvcTemplateDir只能在goal: generate-mvc中运行, 因此profile: " + profile.getName() + "将终止执行");
            return;
        }

        Map<String, ClassGenerator> map = GenerateUtil.resolveTables(profile);
        for (Map.Entry<String, ClassGenerator> entry : map.entrySet()) {
            writeSource(profile, entry);
        }
    }

    private void writeSource(ProfileProperties profile, Map.Entry<String, ClassGenerator> entry) throws Exception {

        String className = entry.getKey();
        ClassGenerator generator = entry.getValue();

        String packageName = profile.getPackageName();
        List<String> paths = new LinkedList<>(Arrays.asList(packageName.split("\\.")));
        paths.add(className + ".java");
        Path srcFilePath = Paths.get(profile.getOutputDir(), paths.toArray(new String[0]));
        File srcFile = srcFilePath.toFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(srcFile, false)) {
            fileOutputStream.write(generator.applyFeatures().generateContent().getBytes());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}