package com.github.hetianyi.plugin.sample.config;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;

/**
 * 特性：为类添加lombok @Slf4j注解
 *
 * @author Jason He
 */
public class Slf4jFeature extends CustomFeature {

    public Slf4jFeature() {
        super(SlotType.CLASS_START, InsertLocation.AFTER, (s, g) -> {
            g.getImports().add("lombok.extern.slf4j.Slf4j");
            return ImmutableList.of(CodeSlot.of("@Slf4j\n"));
        });
    }
}
