package com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin;

import java.util.Optional;

import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.SlotHelper;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：生成类注释
 *
 * @author Jason He
 */
@Slf4j
public class ClassCommentFeature implements Feature {

    @Override
    public void apply(ClassGenerator generator) {
        log.debug("应用Feature -> ClassCommentFeature");
        String comment = Optional.ofNullable(generator.getTabDef().getComment()).orElse("");
        ImmutableList<CodeSlot> codeSlots = ImmutableList.of(
                CodeSlot.of(comment.replace("\r\n", "\n").replaceAll("\n", " <br>\n * "))
        );
        SlotHelper.insertAfter(generator, SlotType.COMMENT_CONTENT_START, codeSlots.toArray(new Slot[0]));
    }
}
