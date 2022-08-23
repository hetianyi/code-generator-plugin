package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.lombok;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为类添加 {@link lombok.AllArgsConstructor @AllArgsConstructor} 注解
 *
 * @author Jason He
 */
@Slf4j
public class LombokAllArgsConstructorAnnotationFeature extends CustomFeature {
    public LombokAllArgsConstructorAnnotationFeature() {
        super(SlotType.CLASS_START, InsertLocation.AFTER, (s, g) -> {
            g.getImports().add("lombok.AllArgsConstructor");
            return ImmutableList.of(CodeSlot.of("@AllArgsConstructor\n"));
        });
        log.debug("应用Feature -> LombokAllArgsConstructorAnnotationFeature");
    }
}
