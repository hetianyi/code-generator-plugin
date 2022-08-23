package com.github.hetianyi.plugins.generator.pojo.generator.feature;

import java.util.List;

import com.github.hetianyi.boot.ready.common.util.CollectionUtil;
import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.google.common.collect.ImmutableList;

/**
 * 特性：在构造类结构之前添加一些自定义字段
 *
 * @author Jason He
 */
public interface AddFieldFeature extends PreGenerateFeature {

    default List<TableColumn> getAddFields(TableDefinition tabDef) {
        return ImmutableList.of();
    }

    @Override
    default void apply(ClassGenerator generator) {
        List<TableColumn> addFields = this.getAddFields(generator.getTabDef());
        if (!CollectionUtil.isNullOrEmpty(addFields)) {
            generator.getTabDef().getColumns().addAll(addFields);
        }
    }
}
