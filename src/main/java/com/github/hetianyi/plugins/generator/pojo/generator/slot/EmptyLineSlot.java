package com.github.hetianyi.plugins.generator.pojo.generator.slot;

import com.github.hetianyi.plugins.generator.common.SlotType;

/**
 * @author Jason He
 */
public class EmptyLineSlot extends CodeSlot {

    public static final EmptyLineSlot instance = new EmptyLineSlot();

    public static final EmptyLineSlot getInstance() {
        return instance;
    }

    @Override
    public String getContent() {
        return "\n";
    }

    @Override
    public SlotType getType() {
        return SlotType.CODE;
    }
}
