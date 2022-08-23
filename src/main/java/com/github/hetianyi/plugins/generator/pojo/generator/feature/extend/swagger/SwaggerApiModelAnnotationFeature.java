package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.swagger;

import java.util.Optional;

import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.common.SlotType;
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
            g.getImports().add("io.swagger.annotations.ApiModel");
            return ImmutableList.of(CodeSlot.of("@ApiModel(description = \"",
                                                Optional.ofNullable(tabDef.getComment())
                                                        .orElse("")
                                                        .replace("\r\n", "\n")
                                                        .replaceAll("\n", "  "),
                                                "\")\n"));
        });
        log.debug("应用Feature -> SwaggerApiModelAnnotationFeature");
    }
}
