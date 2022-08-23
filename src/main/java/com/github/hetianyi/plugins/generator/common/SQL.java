package com.github.hetianyi.plugins.generator.common;

/**
 * schema查询语句
 *
 * @author Jason He
 */
public class SQL {
    // 查询MySQL当前数据库所有表
    public static final String QUERY_MYSQL_TABLES = "select TABLE_NAME, TABLE_TYPE, TABLE_COMMENT from information_schema.`TABLES` a where a.TABLE_SCHEMA = ?";
    // 查询MySQL的表结构
    public static final String QUERY_MYSQL_TABLE_SCHEMA = "select a.ORDINAL_POSITION, a.COLUMN_NAME, a.DATA_TYPE, a.COLUMN_COMMENT, a.COLUMN_KEY, a.EXTRA "
            + "from information_schema.`COLUMNS` a where a.TABLE_SCHEMA=? and a.TABLE_NAME = ? order by a.ORDINAL_POSITION";
    // 查询Postgresql当前数据库所有表
    public static final String QUERY_PG_TABLES = "select a.relname as TABLE_NAME, b.description as TABLE_COMMENT from pg_class a left join ( select * from pg_description where objsubid = 0 ) b on A.oid = b.objoid where a.relname in ( select tablename from pg_tables where schemaname = ? )  order by a.relname asc";
    // 查询Postgresql的表结构
    public static final String QUERY_PG_TABLE_SCHEMA = "SELECT\n"
            + "\tordinal_position AS ORDINAL_POSITION,\n"
            + "\tCOLUMN_NAME AS COLUMN_NAME,\n"
            + "\tdata_type AS DATA_TYPE1,\n"
            + "\tudt_name as DATA_TYPE,\n"
            + "\tCOALESCE ( character_maximum_length, numeric_precision,- 1 ) AS LENGTH,\n"
            + "\tnumeric_scale AS SCALE,\n"
            + "CASE\n"
            + "\t\tis_nullable \n"
            + "\t\tWHEN 'NO' THEN\n"
            + "\t\t0 ELSE 1 \n"
            + "\tEND AS CanNull,\n"
            + "\tcolumn_default AS EXTRA,\n"
            + "CASE\n"
            + "\t\t\n"
            + "\t\tWHEN POSITION ( 'nextval' IN column_default ) > 0 THEN\n"
            + "\t\t1 ELSE 0 \n"
            + "\tEND AS IsIdentity,\n"
            + "CASE\n"
            + "\t\t\n"
            + "\t\tWHEN b.pk_name IS NULL THEN\n"
            + "\t\t'' ELSE 'PRI' \n"
            + "\tEND AS COLUMN_KEY,\n"
            + "\tC.DeText as COLUMN_COMMENT \n"
            + "FROM\n"
            + "\tinformation_schema.\n"
            + "\tCOLUMNS LEFT JOIN (\n"
            + "\tSELECT\n"
            + "\t\tpg_attr.attname AS colname,\n"
            + "\t\tpg_constraint.conname AS pk_name \n"
            + "\tFROM\n"
            + "\t\tpg_constraint\n"
            + "\t\tINNER JOIN pg_class ON pg_constraint.conrelid = pg_class.oid\n"
            + "\t\tINNER JOIN pg_attribute pg_attr ON pg_attr.attrelid = pg_class.oid \n"
            + "\t\tAND pg_attr.attnum = pg_constraint.conkey [ 1 ]\n"
            + "\t\tINNER JOIN pg_type ON pg_type.oid = pg_attr.atttypid \n"
            + "\tWHERE\n"
            + "\t\tpg_class.relname = ? \n"
            + "\t\tAND pg_constraint.contype = 'p' \n"
            + "\t) b ON b.colname = information_schema.COLUMNS.\n"
            + "\tCOLUMN_NAME LEFT JOIN (\n"
            + "\tSELECT\n"
            + "\t\tattname,\n"
            + "\t\tdescription AS DeText \n"
            + "\tFROM\n"
            + "\t\tpg_class\n"
            + "\t\tLEFT JOIN pg_attribute pg_attr ON pg_attr.attrelid = pg_class.oid\n"
            + "\t\tLEFT JOIN pg_description pg_desc ON pg_desc.objoid = pg_attr.attrelid \n"
            + "\t\tAND pg_desc.objsubid = pg_attr.attnum \n"
            + "\tWHERE\n"
            + "\t\tpg_attr.attnum > 0 \n"
            + "\t\tAND pg_attr.attrelid = pg_class.oid \n"
            + "\t\tAND pg_class.relname = ? \n"
            + "\t) C ON C.attname = information_schema.COLUMNS.COLUMN_NAME \n"
            + "WHERE\n"
            + "\ttable_schema = ? \n"
            + "\tand table_name = ? \n"
            + "ORDER BY\n"
            + "\tordinal_position ASC";
}
