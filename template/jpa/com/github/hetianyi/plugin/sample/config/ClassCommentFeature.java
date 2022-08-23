package com.github.hetianyi.plugin.sample.config;

import java.util.Optional;

import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.SlotHelper;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：自定义类注释
 *
 * @author Jason He
 */
@Slf4j
public class ClassCommentFeature implements Feature {

    @Override
    public void apply(ClassGenerator generator) {
        log.debug("应用ClassCommentFeature");
        String prefix = "";
        String profileName = generator.getProfile().getName();
        if (profileName.equalsIgnoreCase("create_form")) {
            prefix = "创建";
        } else if (profileName.equalsIgnoreCase("update_form")) {
            prefix = "更新";
        }
        String comment = Optional.ofNullable(generator.getTabDef().getComment()).orElse("") + "请求表单";


        ImmutableList<CodeSlot> codeSlots = ImmutableList.of(
                CodeSlot.of(prefix, comment.replace("表", "").replace("\r\n", "\n").replaceAll("\n", " <br>\n * "))
        );
        SlotHelper.insertAfter(generator, SlotType.COMMENT_CONTENT_START, codeSlots.toArray(new Slot[0]));
    }
}