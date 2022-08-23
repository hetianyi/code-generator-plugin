package com.github.hetianyi.plugin.sample.config;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：继承Pageable类
 *
 * @author Jason He
 */
@Slf4j
public class AddPageableExtendFeature extends CustomFeature {
    public AddPageableExtendFeature() {
        super(SlotType.EXTENDS_START, InsertLocation.AFTER, (s, g) -> {
            g.getImports().add("com.github.hetianyi.boot.ready.model.http.Pageable");
            return ImmutableList.of(CodeSlot.of("extends Pageable "));
        });
        log.debug("应用Feature -> AddPageableExtendFeature");
    }
}
