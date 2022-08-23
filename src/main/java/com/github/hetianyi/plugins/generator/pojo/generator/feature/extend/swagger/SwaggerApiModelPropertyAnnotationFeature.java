package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.swagger;

import com.github.hetianyi.boot.ready.common.util.StringUtil;
import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.IndentSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 特性：为类的字段添加Swagger的{@link io.swagger.annotations.ApiModelProperty @ApiModelProperty} 注解
 *
 * @author Jason He
 */
@Slf4j
public class SwaggerApiModelPropertyAnnotationFeature extends CustomFeature {

    public SwaggerApiModelPropertyAnnotationFeature() {
        super(SlotType.FIELD_HEAD, InsertLocation.AFTER, (s, g) -> {
            TableColumn column = (TableColumn) s.getAttribute("column");
            if (StringUtil.isNullOrEmptyTrimed(column.getComment())) {
                return null;
            }
            g.getImports().add("io.swagger.annotations.ApiModelProperty");
            return ImmutableList.of(IndentSlot.getInstance(),
                                    CodeSlot.of("@ApiModelProperty(\"",
                                                Optional.ofNullable(column.getComment())
                                                        .orElse("")
                                                        .replace("\r\n", "\n")
                                                        .replaceAll("\n", "\\\\n\\\\n"),
                                                "\")\n"));
        });
        log.debug("应用Feature -> SwaggerApiModelPropertyAnnotationFeature");
    }
}
