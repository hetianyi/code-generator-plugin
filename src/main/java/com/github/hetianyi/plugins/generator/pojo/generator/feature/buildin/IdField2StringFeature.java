package com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin;

import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.SlotHelper;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：将数据库ID字段导出为String类型，而不是默认的Long类型
 *
 * @author Jason He
 */
@Slf4j
public class IdField2StringFeature implements Feature {

    @Override
    public void apply(ClassGenerator generator) {
        log.debug("应用Feature -> IdField2StringFeature");
        SlotHelper.scan(generator, (s, g) -> {
            if (s.getType() == SlotType.CODE) {
                TableColumn column = (TableColumn) s.getAttribute("column");
                if (null != column && column.isId()) {
                    return CodeSlot.of("String").addAttribute("column", column);
                }
            }
            return s;
        });
    }
}
