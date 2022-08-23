package com.github.hetianyi.plugins.generator.pojo.generator.feature;

import java.util.List;
import java.util.function.BiFunction;

import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义生成器特性
 * @author Jason He
 */
@Slf4j
public abstract class CustomFeature implements Feature {

    public enum InsertLocation {
        BEFORE,
        AFTER;
    }



    private SlotType type;
    private InsertLocation loc;
    private BiFunction<Slot, ClassGenerator, List<Slot>> slotFunction;

    public CustomFeature(SlotType type, InsertLocation loc, BiFunction<Slot, ClassGenerator, List<Slot>> slotFunction) {
        this.type = type;
        this.loc = loc;
        this.slotFunction = slotFunction;
    }

    @Override
    public void apply(ClassGenerator generator) {
        // log.info("应用FieldAnnotationFeature");
        if (loc == InsertLocation.BEFORE) {
            SlotHelper.insertBefore(generator, type, slotFunction);
        }
        else {
            SlotHelper.insertAfter(generator, type, slotFunction);
        }
    }
}
