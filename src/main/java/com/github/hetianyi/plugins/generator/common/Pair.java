package com.github.hetianyi.plugins.generator.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Pair {
    private String javaTypeName;
    private String importClass;
}
