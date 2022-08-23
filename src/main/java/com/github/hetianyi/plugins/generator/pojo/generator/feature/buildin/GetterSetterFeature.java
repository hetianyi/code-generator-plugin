package com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin;

import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.SlotHelper;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.EmptyLineSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.IndentSlot;
import com.github.hetianyi.plugins.generator.common.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

import static com.github.hetianyi.plugins.generator.common.JavaTypeMapping.typeMappings;

/**
 * 特性：为类的字段添加Getters和Setters方法
 *
 * @author Jason He
 */
@Slf4j
public class GetterSetterFeature implements Feature {

    @Override
    public void apply(ClassGenerator generator) {
        log.debug("应用Feature -> GetterSetterFeature");
        SlotHelper.insertBefore(
                generator,
                SlotType.ALL_FIELD_END,
                (s, g) -> {
                    List<TableColumn> columns = g.getTabDef().getColumns();
                    List<Slot> slots = new LinkedList<>();
                    for (TableColumn column : columns) {
                        String fieldName = GenerateUtil.camel(column.getName(), false);
                        String methodName = GenerateUtil.camel(column.getName(), true);


                        String simpleClassName = generator.getTypeSimpleClassName(typeMappings.get(column.getType()));
                        slots.add(CodeSlot.of("\n\n",
                                              IndentSlot.instance.getContent(),
                                              "public ",
                                              simpleClassName,
                                              " get",
                                              methodName,
                                              "() {\n",
                                              IndentSlot.instance.getDoubleContent(),
                                              "return this.",
                                              fieldName,
                                              ";\n",
                                              IndentSlot.instance.getContent(),
                                              "}"));
                        slots.add(CodeSlot.of("\n\n",
                                              IndentSlot.instance.getContent(),
                                              "public void set",
                                              methodName,
                                              "(",
                                              simpleClassName,
                                              " ",
                                              fieldName,
                                              ") {\n",
                                              IndentSlot.instance.getDoubleContent(),
                                              "this.",
                                              fieldName,
                                              " = ",
                                              fieldName,
                                              ";\n",
                                              IndentSlot.instance.getContent(),
                                              "}"));
                    }

                    slots.add(EmptyLineSlot.getInstance());
                    return slots;
                });
    }
}
