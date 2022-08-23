package com.github.hetianyi.plugins.generator.pojo.generator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.hetianyi.boot.ready.common.Const;
import com.github.hetianyi.boot.ready.common.util.CollectionUtil;
import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.common.InstanceConfig;
import com.github.hetianyi.plugins.generator.common.ProfileProperties;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.common.util.GenerateUtil;
import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.PreGenerateFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin.ClassCommentFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin.CopyrightFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin.FieldCommentFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin.GetterSetterFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin.IdField2StringFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin.ImportFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.buildin.ProjectBroadcastCommentFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jackson.DateFormatAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jackson.IgnoreNullFieldAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jackson.JasonFormatAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jpa.JpaClassAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jpa.JpaFieldAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.lombok.LombokAllArgsConstructorAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.lombok.LombokBuilderAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.lombok.LombokDataAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.lombok.LombokNoArgsConstructorAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.mybatis.MyBatisPlusClassAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.mybatis.MyBatisPlusFieldAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.ready.ConvertibleFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.swagger.SwaggerApiModelAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.swagger.SwaggerApiModelPropertyAnnotationFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.EmptyLineSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.IndentSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.MarkupSlot;
import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.github.hetianyi.plugins.generator.common.JavaTypeMapping.importMappings;
import static com.github.hetianyi.plugins.generator.common.JavaTypeMapping.typeMappings;

/**
 * Java类生成器
 *
 * @author Jason He
 */
@Slf4j
@Data
public class ClassGenerator {

    private TableDefinition tabDef;
    private ProfileProperties profile;
    private List<Slot> slots;
    private Set<String> imports;
    private Set<String> implementNames;
    private String className;
    private String lowerObjectName; // 没有后缀的小写开头的class名称
    private String upperObjectName; // 没有后缀的大写开头的class名称

    // 必须放在最后的feature
    public static final String MUST_LAST_FEATURE = "ImportFeature";
    public static final List<String> staticFeatures = ImmutableList.of(
            "ClassCommentFeature",
            "ProjectBroadcastCommentFeature",
            "CopyrightFeature",
            "FieldCommentFeature",
            "GetterSetterFeature",
            "ImportFeature"
    );

    private List<Feature> features = new LinkedList<>();

    private static final Map<String, Feature> buildInFeatureMap = new HashMap<String, Feature>() {
        {
            put("ClassCommentFeature", new ClassCommentFeature());
            put("ProjectBroadcastCommentFeature", new ProjectBroadcastCommentFeature());
            put("CopyrightFeature", new CopyrightFeature());
            put("FieldCommentFeature", new FieldCommentFeature());
            put("GetterSetterFeature", new GetterSetterFeature());
            put("ImportFeature", new ImportFeature());
            put("IdField2StringFeature", new IdField2StringFeature());

            put("JasonFormatAnnotationFeature", new JasonFormatAnnotationFeature());
            put("DateFormatAnnotationFeature", new DateFormatAnnotationFeature());
            put("IgnoreNullFieldAnnotationFeature", new IgnoreNullFieldAnnotationFeature());

            put("LombokAllArgsConstructorAnnotationFeature", new LombokAllArgsConstructorAnnotationFeature());
            put("LombokBuilderAnnotationFeature", new LombokBuilderAnnotationFeature());
            put("LombokDataAnnotationFeature", new LombokDataAnnotationFeature());
            put("LombokNoArgsConstructorAnnotationFeature", new LombokNoArgsConstructorAnnotationFeature());

            put("MyBatisPlusClassAnnotationFeature", new MyBatisPlusClassAnnotationFeature());
            put("MyBatisPlusFieldAnnotationFeature", new MyBatisPlusFieldAnnotationFeature());

            put("JpaClassAnnotationFeature", new JpaClassAnnotationFeature());
            put("JpaFieldAnnotationFeature", new JpaFieldAnnotationFeature());

            put("ConvertibleFeature", new ConvertibleFeature());

            put("SwaggerApiModelAnnotationFeature", new SwaggerApiModelAnnotationFeature());
            put("SwaggerApiModelPropertyAnnotationFeature", new SwaggerApiModelPropertyAnnotationFeature());
        }
    };

    private ClassGenerator(TableDefinition tabDef, ProfileProperties profile) {
        this.tabDef = tabDef;
        this.profile = profile;
        this.slots = new LinkedList<>();
        this.imports = new HashSet<>();
        checkFeatures(profile);

        log.debug("----- 使用的 features -----");
        log.debug(Const.GSON.toJson(features.stream().map(v -> v.getClass()
                                                                .getSimpleName())
                                            .collect(Collectors.toList())));
    }

    // 创建一个新的ClassGenerator
    public static ClassGenerator from(TableDefinition tabDef, ProfileProperties profile) {
        return new ClassGenerator(tabDef, profile);
    }

    // 解析装配特性配置
    private void checkFeatures(ProfileProperties profile) {

        List<String> candidates = new LinkedList<>(staticFeatures);

        List<String> providedFeatures = profile.getFeatures();
        if (CollectionUtil.isNullOrEmpty(providedFeatures)) {
            log.info("没有应用的特性");
            return;
        }

        List<String> ignoreFeatures = new LinkedList<>();

        for (String f : providedFeatures) {
            if (StringUtil.isNullOrEmpty(f)) {
                continue;
            }
            if (candidates.contains(f)) {
                continue;
            }
            if (f.startsWith("-")) {
                log.debug("排除feature: {}", f.substring(1));
                candidates.remove(f.substring(1));
                continue;
            }
            if (staticFeatures.contains(f)) {
                continue;
            }
            if (!buildInFeatureMap.containsKey(f)) {
                try {
                    Class<?> featureClass = InstanceConfig.getClassLoader().loadClass(f);
                    if (!Feature.class.isAssignableFrom(featureClass)) {
                        throw new RuntimeException("类\"" + f + "\"不是有效的feature, 必须继承CustomFeature类或实现Feature接口");
                    }
                }
                catch (ClassNotFoundException e) {
                    throw new RuntimeException("无法加载feature: " + f);
                }
            }
            candidates.add(f);
        }

        // 将 ImportFeature 放在尾部
        candidates.remove(MUST_LAST_FEATURE);
        candidates.add(MUST_LAST_FEATURE);

        for (String candidate : candidates) {
            Feature feature = buildInFeatureMap.get(candidate);
            if (null != feature) {
                features.add(feature);
            }
            else {
                try {
                    Object o = InstanceConfig.getClassLoader().loadClass(candidate).newInstance();
                    features.add((Feature) o);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private void handlerPreGenerateFeatures() {
        this.features.stream()
                     .filter(v -> v instanceof PreGenerateFeature)
                     .forEach(v -> v.apply(this));
    }

    // 生成class源代码基本结构
    public ClassGenerator generateStructure() {
        // 执行前置生成器
        handlerPreGenerateFeatures();

        this.className = GenerateUtil.camel(GenerateUtil.removePrefixOrSuffix(tabDef.getName()), true);
        this.upperObjectName = this.className;
        this.lowerObjectName = GenerateUtil.camel(GenerateUtil.removePrefixOrSuffix(tabDef.getName()), false);
        if (!StringUtil.isNullOrEmpty(this.profile.getAppendSuffix())) {
            this.className = StringUtil.trimSafe(this.profile.getAppendPrefix())
                    + className
                    + StringUtil.trimSafe(this.profile.getAppendSuffix());
        }
        log.info("解析Class: {}", this.className);

        // Java包
        slots.add(MarkupSlot.of(SlotType.COPYRIGHT_START));
        slots.add(MarkupSlot.of(SlotType.COPYRIGHT_END));
        slots.add(MarkupSlot.of(SlotType.PACKAGE_START));
        slots.add(CodeSlot.of("package ", profile.getPackageName(), ";"));
        slots.add(EmptyLineSlot.getInstance());
        slots.add(EmptyLineSlot.getInstance());
        slots.add(MarkupSlot.of(SlotType.PACKAGE_END));

        // Java依赖包导入
        slots.add(MarkupSlot.of(SlotType.IMPORT_START));
        slots.add(MarkupSlot.of(SlotType.IMPORT_END));

        // 空行
        slots.add(EmptyLineSlot.getInstance());

        // 类注释
        slots.add(CodeSlot.of("/**"));
        slots.add(EmptyLineSlot.getInstance());
        slots.add(CodeSlot.of(" * "));
        slots.add(MarkupSlot.of(SlotType.COMMENT_CONTENT_START));
        slots.add(MarkupSlot.of(SlotType.COMMENT_CONTENT_END));
        if (!StringUtil.isNullOrEmpty(profile.getAuthor())) {
            slots.add(EmptyLineSlot.getInstance());
            slots.add(CodeSlot.of(" * "));
            slots.add(EmptyLineSlot.getInstance());
            slots.add(CodeSlot.of(" * @author "));
            slots.add(MarkupSlot.of(SlotType.AUTHOR));
            slots.add(CodeSlot.of(profile.getAuthor()));
        }
        slots.add(EmptyLineSlot.getInstance());
        slots.add(CodeSlot.of(" */"));
        slots.add(EmptyLineSlot.getInstance());


        // Class定义
        slots.add(MarkupSlot.of(SlotType.CLASS_START).addAttribute("table", this.tabDef));
        slots.add(CodeSlot.of("public class "));
        slots.add(MarkupSlot.of(SlotType.CLASS_NAME_START));
        slots.add(CodeSlot.of(this.className));
        slots.add(MarkupSlot.of(SlotType.CLASS_NAME_END));
        slots.add(CodeSlot.of(" "));
        slots.add(MarkupSlot.of(SlotType.EXTENDS_START));
        slots.add(MarkupSlot.of(SlotType.EXTENDS_END));
        slots.add(MarkupSlot.of(SlotType.IMPLEMENTS_START));
        slots.add(MarkupSlot.of(SlotType.IMPLEMENTS_END));
        slots.add(CodeSlot.of("{"));
        slots.add(MarkupSlot.of(SlotType.CLASS_FIRST_LINE));

        slots.add(EmptyLineSlot.getInstance());

        // 字段
        List<TableColumn> columns = tabDef.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn column = columns.get(i);
            String type = typeMappings.get(column.getType());
            if (StringUtil.isNullOrEmpty(type)) {
                throw new RuntimeException("无法找到数据库字段类型的Java类型映射：" + column.getType() + " -> ?");
            }
            slots.add(MarkupSlot.of(SlotType.FIELD_START).addAttribute("column", column));

            slots.add(MarkupSlot.of(SlotType.FIELD_HEAD).addAttribute("column", column));
            slots.add(IndentSlot.getInstance());
            slots.add(CodeSlot.of("private "));
            slots.add(CodeSlot.of(type).addAttribute("column", column));
            slots.add(CodeSlot.of(" "));
            slots.add(MarkupSlot.of(SlotType.FIELD_NAME_START));

            column.setFieldName(GenerateUtil.camel(column.getName(), false));

            slots.add(CodeSlot.of(column.getFieldName()).addTag("fieldName"));
            slots.add(MarkupSlot.of(SlotType.FIELD_NAME_END));
            slots.add(CodeSlot.of(";"));
            slots.add(MarkupSlot.of(SlotType.FIELD_TAIL).addAttribute("column", column));

            if (i != columns.size() - 1) {
                slots.add(EmptyLineSlot.getInstance());
            }
            slots.add(MarkupSlot.of(SlotType.FIELD_END));
            if (!StringUtil.isNullOrEmpty(importMappings.get(column.getType()))) {
                imports.add(importMappings.get(column.getType()));
            }
        }

        slots.add(MarkupSlot.of(SlotType.ALL_FIELD_END));
        slots.add(MarkupSlot.of(SlotType.CLASS_LAST_LINE));
        slots.add(EmptyLineSlot.getInstance());
        slots.add(CodeSlot.of("}"));
        slots.add(MarkupSlot.of(SlotType.CLASS_END));
        return this;
    }

    /**
     * 生成基本的class和附带的slot点位
     */
    public String generateContent() {
        StringBuilder sb = new StringBuilder();
        slots.forEach(v -> sb.append(v.getContent()));
        return sb.toString()
                 .replaceAll("\r\n", this.profile.getLineSeparator())
                 .replaceAll("\n", this.profile.getLineSeparator());
    }

    /**
     * 应用特性
     */
    public ClassGenerator applyFeatures() {
        if (CollectionUtil.isNullOrEmpty(slots)) {
            throw new IllegalStateException("请先调用方法: generateClass");
        }

        features.forEach(v -> v.apply(this));
        return this;
    }

    public String getTypeSimpleClassName(String type) {
        return type.substring(type.lastIndexOf(".") + 1);
    }
}
