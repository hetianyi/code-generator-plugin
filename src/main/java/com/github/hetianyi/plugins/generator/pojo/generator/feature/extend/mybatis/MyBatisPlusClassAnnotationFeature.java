package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.mybatis;

import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为类添加 Mybatisplus {@link com.baomidou.mybatisplus.annotation.TableName @com.baomidou.mybatisplus.annotation.TableName} 注解
 *
 * @author Jason He
 */
@Slf4j
public class MyBatisPlusClassAnnotationFeature extends CustomFeature {

    public MyBatisPlusClassAnnotationFeature() {
        super(SlotType.CLASS_START, InsertLocation.AFTER, (s, g) -> {
            TableDefinition tabDef = g.getTabDef();
            g.getImports().add("com.baomidou.mybatisplus.annotation.TableName");
            return ImmutableList.of(CodeSlot.of("@TableName(\"", tabDef.getName(), "\")\n"));
        });
        log.debug("应用Feature -> MyBatisPlusClassAnnotationFeature");
    }
}
