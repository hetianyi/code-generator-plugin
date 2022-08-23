package com.github.hetianyi.plugins.generator.pojo.generator.slot;

import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jason He
 */
public class MarkupSlot implements Slot {

    private SlotType slotType;
    private Map<String, Object> attributes = new HashMap<>();
    private Set<String> tags = new HashSet<>();

    MarkupSlot(SlotType slotType) {
        this.slotType = slotType;
    }

    public static MarkupSlot of(SlotType type) {
        return new MarkupSlot(type);
    }

    @Override
    public Slot addAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    @Override
    public Slot addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    @Override
    public boolean hasTag(String tag) {
        return this.tags.contains(tag);
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public SlotType getType() {
        return slotType;
    }
}
