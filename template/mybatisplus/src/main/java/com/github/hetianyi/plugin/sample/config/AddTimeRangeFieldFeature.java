package com.github.hetianyi.plugin.sample.config;

import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.AddFieldFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * 特性：为字段created_time和updated_time添加范围查询字段
 *
 * @author Jason He
 */
@Slf4j
public class AddTimeRangeFieldFeature implements AddFieldFeature {

    @Override
    public List<TableColumn> getAddFields(TableDefinition tabDef) {

        List<TableColumn> result = new LinkedList<>();
        int pos = tabDef.getColumns().size();
        for (TableColumn column : tabDef.getColumns()) {
            if (column.getName().equalsIgnoreCase("created_time")) {
                result.add(TableColumn.builder()
                        .name("created_time_start")
                        .id(false)
                        .autoIncrement(false)
                        .comment("起始" + column.getComment())
                        .position(pos++)
                        .type(column.getType())
                        .build());
                result.add(TableColumn.builder()
                        .name("created_time_end")
                        .id(false)
                        .autoIncrement(false)
                        .comment("截止" + column.getComment())
                        .position(pos++)
                        .type(column.getType())
                        .build());
            } else if (column.getName().equalsIgnoreCase("updated_time")) {
                result.add(TableColumn.builder()
                        .name("updated_time_start")
                        .id(false)
                        .autoIncrement(false)
                        .comment("起始" + column.getComment())
                        .position(pos++)
                        .type(column.getType())
                        .build());
                result.add(TableColumn.builder()
                        .name("updated_time_end")
                        .id(false)
                        .autoIncrement(false)
                        .comment("截止" + column.getComment())
                        .position(pos++)
                        .type(column.getType())
                        .build());
            }
        }
        return result;
    }
}
