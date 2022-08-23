package com.github.hetianyi.plugin.sample.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.hetianyi.plugins.generator.common.JavaTypeMapping;
import com.github.hetianyi.plugins.generator.common.Slot;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.ClassGenerator;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.Feature;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.SlotHelper;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：自定义类注释
 *
 * @author Jason He
 */
@Slf4j
public class ClassCommentFeature implements Feature {

    /** 默认日期时间格式 */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 默认日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /** 默认时间格式 */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @SneakyThrows
    @Override
    public void apply(ClassGenerator generator) {
        log.debug("应用 ClassCommentFeature");
        String prefix = "";
        String profileName = generator.getProfile().getName();
        if (profileName.equalsIgnoreCase("create_form")) {
            prefix = "创建";
        }
        else if (profileName.equalsIgnoreCase("update_form")) {
            prefix = "更新";
        }
        else if (profileName.equalsIgnoreCase("query_form")) {
            prefix = "查询";
        }
        String comment = Optional.ofNullable(generator.getTabDef().getComment())
                                 .orElse("")
                                 .replace("表", "")
                                 .replace("\r\n", "\n")
                                 .replaceAll("\n", " <br>\n * ") + "请求表单";

        // 请求示例
        Map<String, Object> demo = new HashMap<>();
        generator.getTabDef()
                 .getColumns()
                 .forEach(v -> demo.put(v.getFieldName(),
                                        generateDemoValueFromType(JavaTypeMapping.typeMappings.get(v.getType())))
                 );

        /*ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class,new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());

        String example = "\n * \n * <br><br>\n * 请求表单示例:\n * <pre><code>\n * " +
                objectMapper.writeValueAsString(demo).replaceAll("\n", " * ") +
                "\n * </code></pre>";*/


        ImmutableList<CodeSlot> codeSlots = ImmutableList.of(
                CodeSlot.of(prefix, comment)
        );
        SlotHelper.insertAfter(generator, SlotType.COMMENT_CONTENT_START, codeSlots.toArray(new Slot[0]));
    }


    private static final Map<String, Object> typeDemoValueMap = new HashMap<String, Object>() {
        {
            put("Integer", 100);
            put("Long", 20000101);
            put("Double", 20.01D);
            put("Float", 12.34);
            put("String", "Example");
            put("byte[]", new byte[] {69, 120, 97, 109, 112, 108, 101});
            put("Date", new Date());
            put("LocalDate", LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                                          Calendar.getInstance().get(Calendar.MONTH) + 1,
                                          Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
            put("LocalTime", LocalTime.of(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                          Calendar.getInstance().get(Calendar.MINUTE),
                                          Calendar.getInstance().get(Calendar.SECOND)));
            put("BigDecimal", new BigDecimal("9999"));
        }
    };

    private Object generateDemoValueFromType(String javaType) {
        return typeDemoValueMap.get(javaType);
    }
}