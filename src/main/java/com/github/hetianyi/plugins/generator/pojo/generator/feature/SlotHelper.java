package com.github.hetianyi.plugins.generator.pojo.generator.feature;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import com.github.hetianyi.boot.ready.common.util.CollectionUtil;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * Slot工具类
 *
 * @author Jason He
 */
@Slf4j
public class SlotHelper {

    /**
     * 迭代扫描slots，找到字段Field范围的slot，通常用于替换slots或者删除slots
     *
     * @param generator {@link ClassGenerator ClassGenerator}
     * @param slotFunction 传入字段范围内所有的slot，和{@link ClassGenerator ClassGenerator}，返回一个新的slot列表，用于替换，或者返回空列表，用于删除该字段
     */
    public static void replaceFieldRange(
            ClassGenerator generator,
            BiFunction<List<Slot>, ClassGenerator, List<Slot>> slotFunction
    ) {
        List<Slot> originSlots = generator.getSlots();
        if (CollectionUtil.isNullOrEmpty(originSlots)) {
            return;
        }


        int size = originSlots.size();
        int start = -1, end = -1;
        for (int i = size - 1; i >= 0; i--) {
            Slot s = originSlots.get(i);
            if (s.getType() == SlotType.FIELD_END) {
                if (start != -1) {
                    throw new RuntimeException("结构错误");
                }
                end = i;
            }
            if (s.getType() == SlotType.FIELD_START) {
                if (end == -1) {
                    throw new RuntimeException("结构错误");
                }
                start = i;

                // log.info("找到field范围: [{}, {}], 当前originSlots总数: {}", start, end, originSlots.size());

                List<Slot> fieldRangeSlots = ImmutableList.copyOf(originSlots.subList(start, end + 1));
                List<Slot> replacedSlots = slotFunction.apply(fieldRangeSlots, generator);

                // log.info("移除总数: {}", fieldRangeSlots.size());

                for (int j = start; j <= end; j++) {
                    originSlots.remove(start);
                }
                // log.info("找到field范围: [{}, {}], 当前originSlots总数: {}", start, end, originSlots.size());

                if (!CollectionUtil.isNullOrEmpty(replacedSlots)) {
                    // log.info("返回总数: {}", replacedSlots.size());
                    originSlots.addAll(start + 1, replacedSlots);
                }
                start = -1;
                end = -1;
            }
        }
    }

    /**
     * 迭代扫描slots，通常用于替换内容，不能在此进行slot的增删
     */
    public static void scan(
            ClassGenerator generator,
            BiFunction<Slot, ClassGenerator, Slot> slotFunction
    ) {
        List<Slot> originSlots = generator.getSlots();
        if (CollectionUtil.isNullOrEmpty(originSlots)) {
            return;
        }

        for (int i = 0; i < originSlots.size(); i++) {
            Slot s = originSlots.get(i);
            Slot newSlot = slotFunction.apply(s, generator);
            originSlots.set(i, newSlot);
        }
    }

    public static List<Slot> insertBefore(
            ClassGenerator generator,
            SlotType markup,
            BiFunction<Slot, ClassGenerator, List<Slot>> slotFunction
    ) {
        List<Slot> originSlots = generator.getSlots();
        if (CollectionUtil.isNullOrEmpty(originSlots)) {
            return originSlots;
        }

        int cursor = 0;

        while (true) {
            Slot s = originSlots.get(cursor);
            if (s.getType() == markup) {
                List<Slot> slots = Optional.ofNullable(slotFunction.apply(s, generator)).orElse(ImmutableList.of());
                for (Slot slot : slots) {
                    originSlots.add(cursor, slot);
                    cursor++;
                }
            }
            cursor++;
            if (cursor >= originSlots.size()) {
                break;
            }
        }

        return originSlots;
    }

    public static List<Slot> insertAfter(
            ClassGenerator generator,
            SlotType markup,
            BiFunction<Slot, ClassGenerator, List<Slot>> slotFunction
    ) {
        List<Slot> originSlots = generator.getSlots();
        if (CollectionUtil.isNullOrEmpty(originSlots)) {
            return originSlots;
        }

        int cursor = 0;

        while (true) {
            Slot s = originSlots.get(cursor);
            if (s.getType() == markup) {
                List<Slot> slots = Optional.ofNullable(slotFunction.apply(s, generator)).orElse(ImmutableList.of());
                for (Slot slot : slots) {
                    originSlots.add(cursor + 1, slot);
                    cursor++;
                }
            }
            cursor++;
            if (cursor >= originSlots.size()) {
                break;
            }
        }

        return originSlots;
    }

    public static List<Slot> insertBefore(ClassGenerator generator, SlotType markup, Slot... slots) {

        List<Slot> originSlots = generator.getSlots();
        if (CollectionUtil.isNullOrEmpty(originSlots)) {
            return originSlots;
        }

        int cursor = 0;

        while (true) {
            Slot s = originSlots.get(cursor);
            if (s.getType() == markup) {
                for (Slot slot : slots) {
                    originSlots.add(cursor, slot);
                    cursor++;
                }
            }
            cursor++;
            if (cursor >= originSlots.size()) {
                break;
            }
        }

        return originSlots;
    }

    public static List<Slot> insertAfter(ClassGenerator generator, SlotType markup, Slot... slots) {
        List<Slot> originSlots = generator.getSlots();
        if (CollectionUtil.isNullOrEmpty(originSlots)) {
            return originSlots;
        }

        int cursor = 0;

        while (true) {
            Slot s = originSlots.get(cursor);
            if (s.getType() == markup) {
                for (Slot slot : slots) {
                    originSlots.add(cursor + 1, slot);
                    cursor++;
                }
            }
            cursor++;
            if (cursor >= originSlots.size()) {
                break;
            }
        }

        return originSlots;
    }
}
