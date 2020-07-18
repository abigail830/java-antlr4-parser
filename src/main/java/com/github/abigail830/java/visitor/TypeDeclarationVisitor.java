package com.github.abigail830.java.visitor;

import com.github.abigail830.java.model.JType;
import com.github.abigail830.java.parser.ClassParser;
import com.github.abigail830.java.parser.EnumParser;
import com.github.abigail830.java.parser.InterfaceParser;
import com.github.abigail830.java.parser.TypeParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TypeDeclarationVisitor extends com.github.abigail830.java.JavaParserBaseVisitor<List<JType>> {

    @Override
    public List<JType> visitTypeDeclaration(com.github.abigail830.java.JavaParser.TypeDeclarationContext ctx) {

        final List<TypeParser> typeParsers =
                Arrays.asList(new ClassParser(ctx), new InterfaceParser(ctx), new EnumParser(ctx));

        return typeParsers.stream()
                .filter(TypeParser::shouldParse)
                .map(TypeParser::extract)
                .collect(Collectors.toList());
    }

}