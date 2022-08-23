package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.lombok;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为类添加 {@link lombok.Builder @Builder} 注解
 *
 * @author Jason He
 */
@Slf4j
public class LombokBuilderAnnotationFeature extends CustomFeature {
    public LombokBuilderAnnotationFeature() {
        super(SlotType.CLASS_START, InsertLocation.AFTER, (s, g) -> {
            g.getImports().add("lombok.Builder");
            return ImmutableList.of(CodeSlot.of("@Builder\n"));
        });
        log.debug("应用Feature -> LombokBuilderAnnotationFeature");
    }
}
