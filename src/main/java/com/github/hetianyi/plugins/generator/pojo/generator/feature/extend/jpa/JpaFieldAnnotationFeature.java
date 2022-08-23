package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jpa;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.entity.TableColumn;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.IndentSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为字段添加Jpa字段的注解 <br><br>
 * <p>
 * 可能包含<br>
 * {@link javax.persistence.Column @javax.persistence.Column}<br>
 * {@link javax.persistence.Id @javax.persistence.Id}<br>
 * {@link javax.persistence.GeneratedValue @javax.persistence.GeneratedValue}<br>
 * {@link javax.persistence.GenerationType @javax.persistence.GenerationType}<br>
 * {@link org.hibernate.annotations.GenericGenerator @org.hibernate.annotations.GenericGenerator}<br>
 *
 * @author Jason He
 */
@Slf4j
public class JpaFieldAnnotationFeature extends CustomFeature {

    public JpaFieldAnnotationFeature() {
        super(SlotType.FIELD_HEAD, InsertLocation.AFTER, (s, g) -> {
            TableColumn column = (TableColumn) s.getAttribute("column");
            g.getImports().add("javax.persistence.Column");
            if (column.isId()) {
                g.getImports().add("javax.persistence.Id");
                g.getImports().add("javax.persistence.GeneratedValue");
                g.getImports().add("javax.persistence.GenerationType");
                if (!column.isAutoIncrement()) {
                    g.getImports().add("org.hibernate.annotations.GenericGenerator");
                }
                return ImmutableList.of(
                        IndentSlot.getInstance(),
                        CodeSlot.of("@Id\n"),
                        IndentSlot.getInstance(),
                        CodeSlot.of("@Column(name = \"", column.getName(), "\")\n"),
                        IndentSlot.getInstance(),
                        CodeSlot.of("@GeneratedValue(strategy = GenerationType.",
                                    column.isAutoIncrement()
                                    ? "IDENTITY)\n"
                                    : "AUTO, generator = \"assigned\")\n" +
                                            IndentSlot.getInstance().getContent() +
                                            "@GenericGenerator(name = \"assigned\", strategy = " + "\"assigned\")\n"));
            }
            else {
                return ImmutableList.of(IndentSlot.getInstance(),
                                        CodeSlot.of("@Column(name = \"", column.getName(), "\")\n"));
            }
        });
        log.debug("应用Feature -> JpaFieldAnnotationFeature");
    }
}
