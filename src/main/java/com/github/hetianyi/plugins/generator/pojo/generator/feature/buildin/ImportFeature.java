package com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin;

import com.github.hetianyi.boot.ready.common.Const;
import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.SlotHelper;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.EmptyLineSlot;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 特性：导入依赖
 *
 * @author Jason He
 */
@Slf4j
public class ImportFeature implements Feature {


    @Override
    public void apply(ClassGenerator generator) {

        log.debug("应用Feature -> ImportFeature");

        Set<String> imports = generator.getImports();

        List<CodeSlot> javaImports = imports.stream()
                                            .filter(v -> !StringUtil.isNullOrEmptyTrimed(v))
                                            .filter(v -> v.startsWith("java."))
                                            .sorted()
                                            .map(v -> CodeSlot.of("import ", v, ";", "\n"))
                                            .collect(Collectors.toList());

        log.debug("importSlotList=>{}", Const.GSON.toJson(javaImports));

        SlotHelper.insertBefore(generator, SlotType.IMPORT_START, javaImports.toArray(new Slot[0]));
        SlotHelper.insertBefore(generator, SlotType.IMPORT_START, EmptyLineSlot.instance);

        List<CodeSlot> importSlotList = imports.stream()
                                               .filter(v -> !StringUtil.isNullOrEmptyTrimed(v))
                                               .filter(v -> !v.startsWith("java."))
                                               .sorted()
                                               .map(v -> CodeSlot.of("import ", v, ";", "\n"))
                                               .collect(Collectors.toList());

        log.debug("importSlotList=>{}", Const.GSON.toJson(importSlotList));

        SlotHelper.insertAfter(generator, SlotType.IMPORT_START, importSlotList.toArray(new Slot[0]));
    }
}
