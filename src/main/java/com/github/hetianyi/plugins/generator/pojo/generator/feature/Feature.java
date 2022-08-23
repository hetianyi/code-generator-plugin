package com.github.hetianyi.plugins.generator.pojo.generator.feature;

import java.util.HashMap;
import java.util.Map;

import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;

/**
 * 生成器特性
 *
 * @author Jason He
 */
public interface Feature {
    Map<String, String> params = new HashMap<>();
    default void apply(ClassGenerator generator) {}
}
