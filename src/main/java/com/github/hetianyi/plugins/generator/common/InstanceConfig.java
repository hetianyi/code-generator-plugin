package com.github.hetianyi.plugins.generator.common;

import cn.hutool.db.Db;

import java.util.List;

/**
 * 运行实例配置
 *
 * @author Jason He
 */
public class InstanceConfig {
    private static DbConfigurationProperties dbProperties;
    private static List<String> activeProfiles;
    private static List<ProfileProperties> profileProperties;
    private static Db db;
    private static ClassLoader classLoader;

    public static DbConfigurationProperties getDbProperties() {
        return dbProperties;
    }

    public static void setDbProperties(DbConfigurationProperties dbProperties) {
        InstanceConfig.dbProperties = dbProperties;
    }

    public static List<String> getActiveProfiles() {
        return activeProfiles;
    }

    public static void setActiveProfiles(List<String> activeProfiles) {
        InstanceConfig.activeProfiles = activeProfiles;
    }

    public static List<ProfileProperties> getProfileProperties() {
        return profileProperties;
    }

    public static void setProfileProperties(List<ProfileProperties> profileProperties) {
        InstanceConfig.profileProperties = profileProperties;
    }

    public static Db getDb() {
        return db;
    }

    public static void setDb(Db db) {
        InstanceConfig.db = db;
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    public static void setClassLoader(ClassLoader classLoader) {
        InstanceConfig.classLoader = classLoader;
    }
}
