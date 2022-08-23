package com.github.hetianyi.plugin.sample.config;

import java.util.Optional;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为类添加Swagger的{@link io.swagger.annotations.ApiModel @ApiModel} 注解
 *
 * @author Jason He
 */
@Slf4j
public class SwaggerApiModelAnnotationFeature extends CustomFeature {

    public SwaggerApiModelAnnotationFeature() {
        super(SlotType.CLASS_START, InsertLocation.AFTER, (s, g) -> {
            TableDefinition tabDef = g.getTabDef();
            String prefix = "";
            String profileName = g.getProfile().getName();
            if (profileName.equalsIgnoreCase("create_form")) {
                prefix = "创建";
            } else if (profileName.equalsIgnoreCase("update_form")) {
                prefix = "更新";
            }
            g.getImports().add("io.swagger.annotations.ApiModel");
            return ImmutableList.of(CodeSlot.of("@ApiModel(description = \"",
                                                prefix,
                                                Optional.ofNullable(tabDef.getComment())
                                                        .orElse("")
                                                        .replace("表", "")
                                                        .replace("\r\n", "\n")
                                                        .replaceAll("\n", "  "),
                                                "请求表单",
                                                "\")\n"));
        });
        log.debug("应用SwaggerApiModelAnnotationFeature");
    }
}