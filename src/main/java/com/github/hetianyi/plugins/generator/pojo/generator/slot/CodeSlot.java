package com.github.hetianyi.plugins.generator.pojo.generator.slot;

import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jason He
 */
public class CodeSlot implements Slot {

    private String[] code;
    private Map<String, Object> attributes = new HashMap<>();
    private Set<String> tags = new HashSet<>();

    CodeSlot(String... code) {
        this.code = code;
    }

    public static CodeSlot of(String... code) {
        return new CodeSlot(code);
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
        return StringUtil.join(code);
    }

    @Override
    public SlotType getType() {
        return SlotType.CODE;
    }
}
