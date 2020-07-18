package com.github.abigail830.java.visitor;

import com.github.abigail830.java.model.JClass;
import com.github.abigail830.java.model.JInterface;
import com.github.abigail830.java.model.JType;

public class TypeDeclarationVisitor extends com.github.abigail830.java.JavaParserBaseVisitor<JType> {

    @Override
    public JType visitTypeDeclaration(com.github.abigail830.java.JavaParser.TypeDeclarationContext ctx) {
        if(ctx.classDeclaration()!=null){
            String className = ctx.classDeclaration().IDENTIFIER().getText();
//            final List<String> annotations = ctx.classDeclaration().typeList().typeType().stream()
//                    .map(typeTypeContext -> typeTypeContext.annotation().getText())
//                    .collect(Collectors.toList());
            return new JClass(className);
        }

        if(ctx.interfaceDeclaration()!=null){
            final String interfaceName = ctx.interfaceDeclaration().IDENTIFIER().getText();
//            final List<String> annotations = ctx.interfaceDeclaration().typeList().typeType().stream()
//                    .map(typeTypeContext -> typeTypeContext.annotation().getText())
//                    .collect(Collectors.toList());
            return new JInterface(interfaceName);
        }
        return new JType();
    }
}