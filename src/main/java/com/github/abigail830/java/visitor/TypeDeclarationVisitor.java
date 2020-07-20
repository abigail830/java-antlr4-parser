package com.github.abigail830.java.visitor;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JType;
import com.github.abigail830.java.parser.ClassParser;
import com.github.abigail830.java.parser.EnumParser;
import com.github.abigail830.java.parser.InterfaceParser;
import com.github.abigail830.java.parser.TypeParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.RuleContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TypeDeclarationVisitor extends com.github.abigail830.java.JavaParserBaseVisitor<List<JType>> {

    @Override
    public List<JType> visitTypeDeclaration(com.github.abigail830.java.JavaParser.TypeDeclarationContext ctx) {
        final List<String> modifiers = extractModifiers(ctx);
        final List<String> annotations = extractTypeAnnotation(ctx);

        final List<TypeParser> typeParsers =
                Arrays.asList(new ClassParser(ctx, modifiers, annotations),
                        new InterfaceParser(ctx, modifiers, annotations),
                        new EnumParser(ctx, modifiers, annotations));
        return typeParsers.stream()
                .filter(TypeParser::shouldParse).map(TypeParser::extract)
                .collect(Collectors.toList());
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