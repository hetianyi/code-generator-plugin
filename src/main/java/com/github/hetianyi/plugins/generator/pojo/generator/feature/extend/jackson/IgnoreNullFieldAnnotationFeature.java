package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jackson;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.IndentSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为类添加 {@link com.fasterxml.jackson.annotation.JsonInclude @JsonInclude(JsonInclude.Include.NON_NULL)} 注解，
 * 序列化时忽略空字段
 *
 * @author Jason He
 */
@Slf4j
public class IgnoreNullFieldAnnotationFeature extends CustomFeature {

    public IgnoreNullFieldAnnotationFeature() {
        super(SlotType.CLASS_START, InsertLocation.AFTER, (s, g) -> {
            g.getImports().add("com.fasterxml.jackson.annotation.JsonInclude");
            return ImmutableList.of(CodeSlot.of("@JsonInclude(JsonInclude.Include.NON_NULL)\n"));
        });
        log.debug("应用Feature -> IgnoreNullFieldAnnotationFeature");
    }
}
