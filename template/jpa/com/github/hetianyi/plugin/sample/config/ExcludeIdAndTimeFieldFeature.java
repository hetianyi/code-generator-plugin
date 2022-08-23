package com.github.hetianyi.plugin.sample.config;

import java.util.Set;

import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.ExcludeFieldFeature;
import com.google.common.collect.ImmutableSet;

/**
 * 特性：Form表单类删除ID和创建时间，更新时间字段
 *
 * @author Jason He
 */
public class ExcludeIdAndTimeFieldFeature implements ExcludeFieldFeature {
    @Override
    public Set<String> getExcludeFields(TableDefinition tabDef) {
        return ImmutableSet.of("id", "created_time", "updated_time");
    }
}
