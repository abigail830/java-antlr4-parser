package com.github.abigail830.java.visitor;

public class ImportDeclarationVisitor extends com.github.abigail830.java.JavaParserBaseVisitor<String> {

    @Override
    public String visitImportDeclaration(com.github.abigail830.java.JavaParser.ImportDeclarationContext ctx) {
        return ctx.qualifiedName().getText();
    }
}