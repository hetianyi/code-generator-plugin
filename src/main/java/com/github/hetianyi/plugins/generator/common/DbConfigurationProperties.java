package com.github.hetianyi.plugins.generator.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库连接配置
 *
 * @author Jason He
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DbConfigurationProperties {
    /**
     * 数据库类型：<br>
     * postgresql|mysql, 默认为mysql，目前只支持mysql
     */
    private String type = "mysql";
    private String host = "127.0.0.1";
    private int port = 3306;
    private String dbName = "test";
    /**
     * postgresql的schema
     */
    private String schema = "public";
    private String username = "root";
    private String password = "123456";
}
