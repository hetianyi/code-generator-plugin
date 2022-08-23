package com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin;

import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.SlotHelper;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.EmptyLineSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.IndentSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：生成字段注释
 *
 * @author Jason He
 */
@Slf4j
public class FieldCommentFeature implements Feature {

    @Override
    public void apply(ClassGenerator generator) {
        log.debug("应用Feature -> FieldCommentFeature");
        SlotHelper.insertBefore(
                generator,
                SlotType.FIELD_HEAD,
                (s, g) -> {
                    TableColumn column = (TableColumn) s.getAttribute("column");
                    if (StringUtil.isNullOrEmpty(column.getComment())) {
                        return ImmutableList.of(EmptyLineSlot.getInstance());
                    }
                    return ImmutableList.of(
                            CodeSlot.of("\n", IndentSlot.getInstance().getContent(),
                                        "/**",
                                        column.getComment().contains("\n")
                                        ? "\n" + IndentSlot.getInstance().getContent()
                                                + " * "
                                                + column.getComment().replace("\r\n", "\n").replaceAll("\n", " <br>\n"
                                                + IndentSlot.getInstance().getContent() + " * ")
                                                + "\n" + IndentSlot.getInstance().getContent()
                                        : " " + column.getComment(),
                                        " */\n")
                            );
                });
    }
}
