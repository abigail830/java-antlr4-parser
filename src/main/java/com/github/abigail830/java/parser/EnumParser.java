package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JEnum;
import com.github.abigail830.java.model.JType;

import java.util.List;

public class EnumParser extends TypeParser {


    private final JavaParser.TypeDeclarationContext ctx;
    private List<String> annotations;
    private List<String> modifiers;

    public EnumParser(JavaParser.TypeDeclarationContext ctx, List<String> modifiers, List<String> annotations) {
        this.ctx = ctx;
        this.annotations = annotations;
        this.modifiers = modifiers;
    }

    @Override
    public Boolean shouldParse() {
        return ctx.enumDeclaration() != null;
    }

    @Override
    public JType extract() {
        final String enumName = ctx.enumDeclaration().IDENTIFIER().getText();
        final Integer lineCount = calculateLineCount(ctx.enumDeclaration());
        return new JEnum(enumName, lineCount, annotations, modifiers);
    }
}
