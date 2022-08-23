package com.github.hetianyi.plugins.generator.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库列信息
 *
 * @author Jason He
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableColumn {
    /**
     * 列序号
     */
    private int position;
    /**
     * 列名称
     */
    private String name;
    /**
     * Java字段名称
     */
    private String fieldName;
    /**
     * 列字段类型
     */
    private String type;
    /**
     * 列备注
     */
    private String comment;
    /**
     * 列是否是主键
     */
    private boolean id;
    /**
     * 列是否递增
     */
    private boolean autoIncrement;
}
