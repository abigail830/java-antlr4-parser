package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JClass;
import com.github.abigail830.java.model.JType;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ClassParser extends TypeParser {

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
        final Integer lineCount = calculateLineCount(ctx.classDeclaration());
        List<String> annotations = extractTypeAnnotation(ctx);
        List<String> modifiers = extractModifiers(ctx);

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


        return new JClass(className, lineCount, annotations, modifiers, typeParams, typeType, typeList);
    }

    private String getTypeType(JavaParser.TypeTypeContext context) {
        return context.classOrInterfaceType().IDENTIFIER().stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining("."));
    }


}
