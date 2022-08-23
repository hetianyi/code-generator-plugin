package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.mybatis;

import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.IndentSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为字段添加Mybatisplus字段的注解 <br><br>
 *
 * 可能包含<br>
 * {@link com.baomidou.mybatisplus.annotation.TableId @com.baomidou.mybatisplus.annotation.TableId}<br>
 * {@link com.baomidou.mybatisplus.annotation.TableField @com.baomidou.mybatisplus.annotation.TableField}<br>
 *
 * @author Jason He
 */
@Slf4j
public class MyBatisPlusFieldAnnotationFeature extends CustomFeature {

    public MyBatisPlusFieldAnnotationFeature() {
        super(SlotType.FIELD_HEAD, InsertLocation.AFTER, (s, g) -> {
            TableColumn column = (TableColumn) s.getAttribute("column");
            if (column.isId()) {
                g.getImports().add("com.baomidou.mybatisplus.annotation.TableId");
                g.getImports().add("com.baomidou.mybatisplus.annotation.IdType");
                return ImmutableList.of(IndentSlot.getInstance(),
                                        CodeSlot.of("@TableId(value = \"",
                                                    column.getName(),
                                                    "\", type = IdType.",
                                                    column.isAutoIncrement() ? "AUTO" : "INPUT", ")\n"));
            }
            else {
                g.getImports().add("com.baomidou.mybatisplus.annotation.TableField");
                return ImmutableList.of(IndentSlot.getInstance(),
                                        CodeSlot.of("@TableField(\"", column.getName(), "\")\n"));
            }
        });
        log.debug("应用Feature -> MyBatisPlusFieldAnnotationFeature");
    }
}
