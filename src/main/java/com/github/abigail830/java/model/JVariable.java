package com.github.abigail830.java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JVariable {

    String annotations;
    String variableType;
    String variableId;
}
