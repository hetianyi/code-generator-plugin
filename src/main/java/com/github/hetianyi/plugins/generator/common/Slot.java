package com.github.hetianyi.plugins.generator.common;

import java.util.Map;

/**
 * 代码插槽
 *
 * @author Jason He
 */
public interface Slot {
    String getContent();
    SlotType getType();
    Slot addAttribute(String key, Object value);
    Slot addTag(String tag);

    Map<String, Object> getAttributes();

    Object getAttribute(String key);
    boolean hasTag(String tag);
}
