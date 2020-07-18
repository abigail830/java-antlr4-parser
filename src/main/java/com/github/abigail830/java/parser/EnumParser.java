package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JEnum;
import com.github.abigail830.java.model.JType;

public class EnumParser extends TypeParser {

    private JavaParser.TypeDeclarationContext ctx;

    public EnumParser(JavaParser.TypeDeclarationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Boolean shouldParse() {
        return ctx.enumDeclaration() != null;
    }

    @Override
    public JType extract() {
        final String enumName = ctx.enumDeclaration().IDENTIFIER().getText();
        final Integer lineCount = calculateLineCount(ctx.enumDeclaration());
        return new JEnum(enumName, lineCount, extractTypeAnnotation(ctx));
    }
}
