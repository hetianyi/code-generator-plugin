package com.github.hetianyi.plugins.generator.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据库表定义信息
 *
 * @author Jason He
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableDefinition {
    /**
     * 表名称
     */
    private String name;
    /**
     * 表类型，实体表 BASE TABLE 或者视图 View
     */
    private String type;
    /**
     * 表注释
     */
    private String comment;
    /**
     * 生成类添加的后缀
     */
    private String appendSuffix;
    /**
     * 列
     */
    private List<TableColumn> columns;
}
