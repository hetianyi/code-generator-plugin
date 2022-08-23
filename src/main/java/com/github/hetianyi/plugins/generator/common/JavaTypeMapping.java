package com.github.hetianyi.plugins.generator.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库字段类型和Java类型映射
 *
 * @author Jason He
 */
public class JavaTypeMapping {
    public static final Map<String, String> typeMappings = new HashMap() {
        {
            put("int", "Integer");
            put("integer", "Integer");
            put("tinyint", "Integer");
            put("smallint", "Integer");
            put("mediumint", "Integer");
            put("bigint", "Long");

            put("double", "Double");
            put("float", "Float");

            put("char", "String");
            put("varchar", "String");
            put("tinytext", "String");
            put("text", "String");
            put("mediumtext", "String");
            put("longtext", "String");
            put("json", "String");

            put("tinyblob", "byte[]");
            put("blob", "byte[]");
            put("mediumblob", "byte[]");
            put("longblob", "byte[]");
            put("datetime", "Date");
            put("date", "LocalDate");
            put("timestamp", "Date");
            put("time", "LocalTime");
            put("decimal", "BigDecimal");
            put("varbinary", "byte[]");
            put("binary", "byte[]");


            // pg types
            put("int2", "Integer");
            put("int4", "Integer");
            put("int8", "Long");
            put("bpchar", "String");
            put("char", "String");
            put("varchar", "String");
            put("text", "String");
            put("json", "String");
            put("jsonb", "String");
            put("decimal", "BigDecimal");
            put("timestamp", "Date");
            put("timestamptz", "Date");
            put("date", "LocalDate");
            put("time", "LocalTime");
            put("timetz", "LocalTime");
            put("float4", "Float");
            put("float8", "Double");

        }
    };

    // 需要导入包的字段类型
    public static final Map<String, String> importMappings = new HashMap() {
        {
            put("datetime", "java.util.Date");
            put("date", "java.time.LocalDate");
            put("timestamp", "java.util.Date");
            put("time", "java.time.LocalTime");
            put("decimal", "java.math.BigDecimal");
        }
    };
}
