package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JEnum;
import com.github.abigail830.java.model.JType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnumParser implements TypeParser {

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
        final int startLine = ctx.enumDeclaration().start.getLine();
        final int stopLine = ctx.enumDeclaration().stop.getLine();
        return new JEnum(enumName, stopLine - startLine, extractTypeAnnotation(ctx));
    }

    private List<String> extractTypeAnnotation(JavaParser.TypeDeclarationContext ctx) {
        List<String> annotations = new ArrayList<>();
        if (ctx.classOrInterfaceModifier() != null) {
            annotations = ctx.classOrInterfaceModifier().stream()
                    .filter(modifer -> modifer.annotation() != null)
                    .map(modifier1 -> modifier1.annotation().qualifiedName().getText())
                    .collect(Collectors.toList());
        }
        return annotations;
    }
}
