package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TypeParser implements JParser {

    public abstract Boolean shouldParse();

    Integer calculateLineCount(ParserRuleContext context) {
        return context.stop.getLine() - context.start.getLine();
    }

    List<String> extractTypeAnnotation(JavaParser.TypeDeclarationContext ctx) {
        List<String> annotations = new ArrayList<>();
        if (ctx.classOrInterfaceModifier() != null) {
            annotations = ctx.classOrInterfaceModifier().stream()
                    .filter(modifer -> modifer.annotation() != null)
                    .map(modifier1 -> modifier1.annotation().qualifiedName().getText())
                    .collect(Collectors.toList());
        }
        return annotations;
    }

    List<String> extractModifiers(JavaParser.TypeDeclarationContext ctx) {
        List<String> modifiers = new ArrayList<>();
        if (ctx.classOrInterfaceModifier() != null) {
            modifiers = ctx.classOrInterfaceModifier().stream()
                    .map(RuleContext::getText)
                    .filter(c -> !c.startsWith("@"))
                    .collect(Collectors.toList());
        }
        return modifiers;
    }

}
