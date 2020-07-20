package com.github.abigail830.java.visitor;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JConst;

public class ConstVisitor extends com.github.abigail830.java.JavaParserBaseVisitor<JConst> {

    @Override
    public JConst visitInterfaceMemberDeclaration(JavaParser.InterfaceMemberDeclarationContext ctx) {
        JavaParser.ConstDeclarationContext constDeclarationContext = ctx.constDeclaration();

        String constType = null;
        String annotation = null;
        String constName = null;

        if (constDeclarationContext.typeType() != null) {
            constType = constDeclarationContext.typeType().getText();
            if (constDeclarationContext.typeType().annotation() != null) {
                annotation = constDeclarationContext.typeType().annotation().getText();
            }
        }
        if (!constDeclarationContext.constantDeclarator().isEmpty()) {
            constName = constDeclarationContext.constantDeclarator(0).IDENTIFIER().getText();
        }
        return new JConst(annotation, constType, constName);
    }

}
