package com.github.hetianyi.plugins.generator.pojo.generator.slot;

import com.github.hetianyi.plugins.generator.common.SlotType;

/**
 * @author Jason He
 */
public class IndentSlot extends CodeSlot {

    public static final IndentSlot instance = new IndentSlot();

    public static final IndentSlot getInstance() {
        return instance;
    }

    @Override
    public String getContent() {
        return "    ";
    }

    public String getDoubleContent() {
        return "        ";
    }

    @Override
    public SlotType getType() {
        return SlotType.CODE;
    }
}
