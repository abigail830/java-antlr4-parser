package com.github.abigail830.java.visitor;

import com.github.abigail830.java.model.JNode;
import com.github.abigail830.java.model.JType;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class NodeVisitor extends com.github.abigail830.java.JavaParserBaseVisitor<JNode> {

    @Override
    public JNode visitCompilationUnit(com.github.abigail830.java.JavaParser.CompilationUnitContext ctx) {
        String pkgName = null;
        List<String> annotations = new ArrayList<>();
        if (ctx.packageDeclaration() != null) {
            pkgName = ctx.packageDeclaration().qualifiedName().getText();
            annotations = ctx.packageDeclaration().annotation()
                    .stream().map(annotationContext -> annotationContext.qualifiedName().getText())
                    .collect(toList());
        }

        List<String> imports = new ArrayList<>();
        if (ctx.importDeclaration() != null) {
            ImportDeclarationVisitor importDeclarationVisitor = new ImportDeclarationVisitor();
            imports = ctx.importDeclaration()
                    .stream()
                    .map(importDeclarationContext -> importDeclarationContext.accept(importDeclarationVisitor))
                    .collect(toList());
        }

        TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
        final List<JType> types = ctx.typeDeclaration().stream()
                .map(typeDeclarationContext -> typeDeclarationContext.accept(typeDeclarationVisitor))
                .flatMap(List::stream).collect(toList());
        return new JNode(pkgName, annotations, imports, types);
    }
}