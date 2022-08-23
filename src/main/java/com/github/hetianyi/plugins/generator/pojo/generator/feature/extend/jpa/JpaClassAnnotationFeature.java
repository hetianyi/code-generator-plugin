package com.github.hetianyi.plugins.generator.pojo.generator.feature.extend.jpa;

import com.github.hetianyi.plugins.generator.common.SlotType;
import com.github.hetianyi.plugins.generator.pojo.entity.TableDefinition;
import com.github.hetianyi.plugins.generator.pojo.generator.feature.CustomFeature;
import com.github.hetianyi.plugins.generator.pojo.generator.slot.CodeSlot;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

/**
 * 特性：为类添加 Jpa
 * {@link javax.persistence.Entity @Entity}
 * 和
 * {@link javax.persistence.Table @Table} 注解
 *
 * @author Jason He
 */
@Slf4j
public class JpaClassAnnotationFeature extends CustomFeature {

    public JpaClassAnnotationFeature() {
        super(SlotType.CLASS_START, InsertLocation.AFTER, (s, g) -> {
            TableDefinition tabDef = g.getTabDef();
            g.getImports().add("javax.persistence.Entity");
            g.getImports().add("javax.persistence.Table");
            return ImmutableList.of(CodeSlot.of("@Entity\n"),
                                    CodeSlot.of("@Table(name = \"", tabDef.getName(), "\")\n"));
        });
        log.debug("应用Feature -> JpaClassAnnotationFeature");
    }
}
