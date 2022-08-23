package com.github.hetianyi.plugins.generator.common;

/**
 * 代码插槽类型
 *
 * @author Jason He
 */
public enum SlotType {
    CODE,
    COPYRIGHT_START,
    COPYRIGHT_END,
    PACKAGE_START,
    PACKAGE_END,
    IMPORT_START,
    IMPORT_END,
    COMMENT_CONTENT_START,
    COMMENT_CONTENT_END,
    AUTHOR,
    CLASS_START,
    CLASS_END,
    CLASS_NAME_START,
    CLASS_NAME_END,
    EXTENDS_START,
    EXTENDS_END,
    IMPLEMENTS_START,
    IMPLEMENTS_END,
    CLASS_FIRST_LINE,
    CLASS_LAST_LINE,
    FIELD_START,
    FIELD_NAME_START,
    FIELD_NAME_END,
    FIELD_HEAD,
    FIELD_TAIL,
    FIELD_END,
    ALL_FIELD_END,
}
