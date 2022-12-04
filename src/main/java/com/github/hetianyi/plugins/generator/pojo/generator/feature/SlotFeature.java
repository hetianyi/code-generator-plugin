package com.github.hetianyi.plugins.generator.pojo.generator.feature;

import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * 特性：运行时动态处理感兴趣的slot（更新、删除等操作）
 *
 * @author Jason He
 */
public interface SlotFeature extends Feature {

    default Slot resolve(Slot slot) {
        return slot;
    }

    @Override
    default void apply(ClassGenerator generator) {
    }
}
