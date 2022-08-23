package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jackson;

import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.IndentSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 特性：为时间日期格式的字段添加 {@link com.fasterxml.jackson.annotation.JsonFormat @JsonFormat} 注解
 *
 * @author Jason He
 */
@Slf4j
public class JasonFormatAnnotationFeature extends CustomFeature {

    public JasonFormatAnnotationFeature() {
        super(SlotType.FIELD_HEAD, InsertLocation.AFTER, (s, g) -> {
            TableColumn column = (TableColumn) s.getAttribute("column");
            String format = "";
            boolean add = false;
            if (Objects.equals(column.getType(), "datetime") || Objects.equals(column.getType(), "timestamp")) {
                format = "yyyy-MM-dd HH:mm:ss";
                add = true;
            } else if (Objects.equals(column.getType(), "date")) {
                format = "yyyy-MM-dd";
                add = true;
            } else if (Objects.equals(column.getType(), "time")) {
                format = "HH:mm:ss";
                add = true;
            }
            if (add) {
                g.getImports().add("com.fasterxml.jackson.annotation.JsonFormat");
                return ImmutableList.of(IndentSlot.getInstance(),
                                        CodeSlot.of("@JsonFormat(pattern = \""+ format +"\")\n"));
            }
            return null;
        });
        log.debug("应用Feature -> JasonFormatAnnotationFeature");
    }
}
