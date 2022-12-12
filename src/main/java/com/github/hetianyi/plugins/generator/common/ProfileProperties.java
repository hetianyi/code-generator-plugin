package com.github.hetianyi.plugins.generator.common;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * profile配置
 *
 * @author Jason He
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileProperties {
    /**
     * 配置名称
     */
    private String name = "dev";
    /**
     * 输出文件夹
     */
    private String outputDir = "./generated";
    /**
     * 输出源文件package
     */
    private String packageName = "com.example";
    /**
     * 作者
     */
    private String author = "";
    /**
     * 版权信息
     */
    private String copyright = "";
    /**
     * 换行符
     * CR|LF|CRLF, 默认CRLF
     */
    private String lineSeparator= "CRLF"; // CR|LF|CRLF, 默认CRLF
    /**
     * 忽略的表前缀
     */
    private Set<String> ignorePrefixes = ImmutableSet.of("t_");
    /**
     * 忽略的表后缀
     */
    private Set<String> ignoreSuffixes = ImmutableSet.of();
    /**
     * 包含的表名称
     */
    private Set<String> includeTables = ImmutableSet.of();
    /**
     * 排除表名称
     */
    private Set<String> excludeTables = ImmutableSet.of();
    /**
     * 生成Java类追加的后缀
     */
    private String appendSuffix = "";
    /**
     * 生成Java类追加的前缀
     */
    private String appendPrefix = "";
    /**
     * 模版文件根目录
     */
    private Set<String> templateDirs = ImmutableSet.of();
    /**
     * 应用特性
     */
    private List<String> features = ImmutableList.of();

    public String getLineSeparator() {
        if ("CR".equalsIgnoreCase(this.lineSeparator)) {
            return "\r";
        }
        if ("LF".equalsIgnoreCase(this.lineSeparator)) {
            return "\n";
        }
        return "\r\n";
    }
}
