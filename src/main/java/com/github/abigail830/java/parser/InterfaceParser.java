package com.github.abigail830.java.parser;

import com.github.abigail830.java.JavaParser;
import com.github.abigail830.java.model.JConst;
import com.github.abigail830.java.model.JInterface;
import com.github.abigail830.java.model.JType;
import com.github.abigail830.java.visitor.ConstVisitor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class InterfaceParser extends TypeParser {

    private JavaParser.TypeDeclarationContext ctx;
    private List<String> annotations;
    private List<String> modifiers;

    public InterfaceParser(JavaParser.TypeDeclarationContext ctx, List<String> modifiers, List<String> annotations) {
        this.ctx = ctx;
        this.annotations = annotations;
        this.modifiers = modifiers;
    }

    @Override
    public Boolean shouldParse() {
        return ctx.interfaceDeclaration() != null;
    }

    @Override
    public JType extract() {
        final String interfaceName = ctx.interfaceDeclaration().IDENTIFIER().getText();
        final Integer lineCount = calculateLineCount(ctx.interfaceDeclaration());

        ConstVisitor constVisitor = new ConstVisitor();

        final List<JConst> consts = ctx.interfaceDeclaration().interfaceBody().interfaceBodyDeclaration()
                .stream()
                .filter(ibContext -> ibContext.interfaceMemberDeclaration() != null)
                .map(JavaParser.InterfaceBodyDeclarationContext::interfaceMemberDeclaration)
                .filter(imContext -> imContext.constDeclaration() != null)
                .map(context -> context.accept(constVisitor))
                .collect(Collectors.toList());

        return new JInterface(interfaceName, lineCount, annotations, modifiers, consts);
    }

}
