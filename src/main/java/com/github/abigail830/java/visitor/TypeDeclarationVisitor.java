package com.github.abigail830.java.visitor;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JClass;
import com.github.abigail830.java.model.JEnum;
import com.github.abigail830.java.model.JInterface;
import com.github.abigail830.java.model.JType;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TypeDeclarationVisitor extends com.github.abigail830.java.JavaParserBaseVisitor<JType> {

    @Override
    public JType visitTypeDeclaration(com.github.abigail830.java.JavaParser.TypeDeclarationContext ctx) {

        if (ctx.classDeclaration() != null) {
            return extractJClass(ctx);
        }

        if (ctx.interfaceDeclaration() != null) {
            return extractInterface(ctx);
        }

        if (ctx.enumDeclaration() != null) {
            final String enumName = extractEnum(ctx);
            final int startLine = ctx.enumDeclaration().start.getLine();
            final int stopLine = ctx.enumDeclaration().stop.getLine();
            return new JEnum(enumName, stopLine - startLine, extractTypeAnnotation(ctx));
        }
        return new JType();
    }

    private String extractEnum(JavaParser.TypeDeclarationContext ctx) {
        return ctx.enumDeclaration().IDENTIFIER().getText();
    }

    private JType extractInterface(JavaParser.TypeDeclarationContext ctx) {
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

    private JType extractJClass(JavaParser.TypeDeclarationContext ctx) {

        final JavaParser.ClassDeclarationContext context = ctx.classDeclaration();

        String className = context.IDENTIFIER().getText();
        final int startLine = context.start.getLine();
        final int stopLine = context.stop.getLine();

        //extents
        String typeType = null;
        if(context.typeType()!=null){
            typeType = getTypeType(context.typeType());
        }

        //implements
        List<String> typeList = new ArrayList<>();
        if(context.typeList()!=null){
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
}