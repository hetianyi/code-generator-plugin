package com.github.hetianyi.plugins.generator.pojo.generator.feature;

import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.common.Pair;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;

import static com.github.hetianyi.plugins.generator.common.JavaTypeMapping.importMappings;
import static com.github.hetianyi.plugins.generator.common.JavaTypeMapping.typeMappings;

/**
 * 特性：运行时动态处理感兴趣的slot（更新、删除等操作）
 *
 * @author Jason He
 */
public interface TypeMappingFeature extends Feature {

    default Pair getType(String dbTypeName, String columnName) {
        String type = typeMappings.get(dbTypeName);
        if (!StringUtil.isNullOrEmpty(importMappings.get(dbTypeName))) {
            return new Pair(type, importMappings.get(dbTypeName));
        }
        return new Pair(type, "");
    }

    @Override
    default void apply(ClassGenerator generator) {
    }
}
