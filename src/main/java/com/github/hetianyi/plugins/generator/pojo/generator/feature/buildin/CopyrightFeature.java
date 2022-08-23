package com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin;

import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.SlotHelper;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 特性：生成版权注释，位于类第一行
 *
 * @author Jason He
 */
@Slf4j
public class CopyrightFeature implements Feature {

    @Override
    public void apply(ClassGenerator generator) {
        log.debug("应用Feature -> CopyrightFeature");
        if (StringUtil.isNullOrEmpty(generator.getProfile().getCopyright())) {
            return;
        }
        List<CodeSlot> slotList = ImmutableList.of(
                CodeSlot.of("/**\n * ",
                            generator.getProfile().getCopyright().replaceAll("\n", "\n * "),
                            "\n */\n\n"
                            )
        );
        SlotHelper.insertAfter(generator, SlotType.COPYRIGHT_START, slotList.toArray(new Slot[0]));
    }
}
