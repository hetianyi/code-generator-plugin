package com.github.hetianyi.plugins.generator.pojo.generator.feature;

import java.util.Iterator;
import java.util.Set;

import com.github.hetianyi.boot.ready.common.util.CollectionUtil;
import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.google.common.collect.ImmutableSet;

/**
 * 特性：在构造类结构之前将某些字段移除
 *
 * @author Jason He
 */
public interface ExcludeFieldFeature extends PreGenerateFeature {

    default Set<String> getExcludeFields(TableDefinition tabDef) {
        return ImmutableSet.of();
    }

    @Override
    default void apply(ClassGenerator generator) {
        Set<String> excludeFields = this.getExcludeFields(generator.getTabDef());
        if (!CollectionUtil.isNullOrEmpty(excludeFields)) {
            for (Iterator<TableColumn> it = generator.getTabDef().getColumns().iterator(); it.hasNext(); ) {
                TableColumn column = it.next();
                if (excludeFields.contains(column.getName())) {
                    it.remove();
                }
            }
        }
    }
}
