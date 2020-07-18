package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JClass;
import com.github.abigail830.java.model.JType;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return new JClass(className, stopLine - startLine, extractTypeAnnotation(ctx), typeParams, typeType, typeList);
    }

    private String getTypeType(JavaParser.TypeTypeContext context) {
        return context.classOrInterfaceType().IDENTIFIER().stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining("."));
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
