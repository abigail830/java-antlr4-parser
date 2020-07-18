package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JClass;
import com.github.abigail830.java.model.JType;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ClassParser implements TypeParser {

    private JavaParser.TypeDeclarationContext ctx;

    public ClassParser(JavaParser.TypeDeclarationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Boolean shouldParse() {
        return ctx.classDeclaration() != null;
    }

    @Override
    public JType extract() {

        final JavaParser.ClassDeclarationContext context = ctx.classDeclaration();

        String className = context.IDENTIFIER().getText();
        final int startLine = context.start.getLine();
        final int stopLine = context.stop.getLine();

        //extents
        String typeType = null;
        if (context.typeType() != null) {
            typeType = getTypeType(context.typeType());
        }

        //implements
        List<String> typeList = new ArrayList<>();
        if (context.typeList() != null) {
            typeList = context.typeList().typeType().stream()
                    .map(this::getTypeType)
                    .collect(Collectors.toList());
        }

        List<String> typeParams = new ArrayList<>();
        if (context.typeParameters() != null) {
            typeParams = context.typeParameters().typeParameter().stream()
                    .map(typeParameterContext -> typeParameterContext.IDENTIFIER().getText())
                    .collect(Collectors.toList());
        }

        List<String> annotations = extractTypeAnnotation();

        List<String> modifiers = extractModifiers(annotations);

        return new JClass(className, stopLine - startLine, annotations, modifiers,
                typeParams, typeType, typeList);
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

    private String getTypeType(JavaParser.TypeTypeContext context) {
        return context.classOrInterfaceType().IDENTIFIER().stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining("."));
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
}
