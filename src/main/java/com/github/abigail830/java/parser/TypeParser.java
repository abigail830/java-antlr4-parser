package com.github.abigail830.java.parser;

import com.github.abigail830.java.model.JType;

public interface TypeParser {

    Boolean shouldParse();

    JType extract();
}
