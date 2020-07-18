package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JInterface;
import com.github.abigail830.java.model.JType;

import java.util.List;

public class InterfaceParser extends TypeParser {

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
        final Integer lineCount = calculateLineCount(ctx.interfaceDeclaration());
        final List<String> annotations = extractTypeAnnotation(ctx);
        List<String> modifiers = extractModifiers(ctx);

        return new JInterface(interfaceName, lineCount, annotations, modifiers);
    }

}
