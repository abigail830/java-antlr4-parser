package com.github.abigail830.java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JNode {

    String pkgName;
    List<String> classAnnotations;
    List<String> jImports;
    List<JType> types;

}
