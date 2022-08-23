package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.ready;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为类添加 {@link com.github.hetianyi.boot.ready.common.Convertible @Convertible} 接口，方便进行类的转换和数据复制。
 *
 * @author Jason He
 */
@Slf4j
public class ConvertibleFeature extends CustomFeature {

    public ConvertibleFeature() {
        super(SlotType.IMPLEMENTS_START, InsertLocation.AFTER, (s, g) -> {
            g.getImports().add("com.github.hetianyi.boot.ready.common.Convertible");
            return ImmutableList.of(CodeSlot.of("implements Convertible<", g.getClassName(), "> "));
        });
        log.debug("应用Feature -> ConvertibleFeature");
    }
}
