package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JInterface;
import com.github.abigail830.java.model.JType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceParser implements TypeParser {

    private JavaParser.TypeDeclarationContext ctx;

    public InterfaceParser(JavaParser.TypeDeclarationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Boolean shouldParse() {
        return ctx.interfaceDeclaration() != null;
    }

    @Override
    public JType extract() {
        final String interfaceName = ctx.interfaceDeclaration().IDENTIFIER().getText();
        final int startLine = ctx.interfaceDeclaration().start.getLine();
        final int stopLine = ctx.interfaceDeclaration().stop.getLine();
        return new JInterface(interfaceName, stopLine - startLine, extractTypeAnnotation(ctx));
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
