package com.github.hetianyi.plugins.generator.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Mvc功能的注释标记
 *
 * @author Jason He
 */
public class TemplateConfig {

    public static final String MVC_MARKER = "#MvcTemplate#";
    public static final String MVC_MAPPER_ALL_FIELDS_MARKER = "/\\*\\s*#allFieldsStart#\\s*\\*/.*/\\*\\s*#allFieldsEnd#\\s*\\*/";
    public static final String MVC_MAPPER_ID_FIELD_MARKER = "/\\*\\s*#idFieldStart#\\s*\\*/.*/\\*\\s*#idFieldEnd#\\s*\\*/";

    /**
     * MVC模版标记
     */
    public static final Map<TemplateType, String> templateCommentMarker = new HashMap() {
        {
            put(TemplateType.CONTROLLER, "#MvcTemplate#");
            put(TemplateType.SERVICE, "#MvcTemplate#");
            put(TemplateType.SERVICE_IMPL, "#MvcTemplate#");
            put(TemplateType.MAPPER, "#MvcTemplate#");
            put(TemplateType.MAPPER_XML, "#MvcTemplate#");
        }
    };
}
