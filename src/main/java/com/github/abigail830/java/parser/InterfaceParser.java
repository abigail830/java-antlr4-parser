package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JInterface;
import com.github.abigail830.java.model.JType;
import org.antlr.v4.runtime.RuleContext;

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
        final Integer lineCount = calculateLineCount();
        final List<String> annotations = extractTypeAnnotation();
        List<String> modifiers = extractModifiers(annotations);

        return new JInterface(interfaceName, lineCount, annotations, modifiers);
    }

    private Integer calculateLineCount() {
        return ctx.interfaceDeclaration().stop.getLine() - ctx.interfaceDeclaration().start.getLine();
    }

    private List<String> extractTypeAnnotation() {
        List<String> annotations = new ArrayList<>();
        if (ctx.classOrInterfaceModifier() != null) {
            annotations = ctx.classOrInterfaceModifier().stream()
                    .filter(modifer -> modifer.annotation() != null)
                    .map(modifier1 -> modifier1.annotation().qualifiedName().getText())
                    .collect(Collectors.toList());
        }
        return annotations;
    }

    private List<String> extractModifiers(List<String> annotations) {
        List<String> modifiers = new ArrayList<>();
        if (ctx.classOrInterfaceModifier() != null) {
            final List<String> allModifiers = ctx.classOrInterfaceModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            modifiers = allModifiers.stream()
                    .filter((m1 -> !annotations.contains(m1)))
                    .collect(Collectors.toList());
        }
        return modifiers;
    }
}
