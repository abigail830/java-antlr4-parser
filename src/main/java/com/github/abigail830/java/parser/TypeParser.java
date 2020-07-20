package com.github.abigail830.java.parser;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class TypeParser implements JParser {

    public abstract Boolean shouldParse();

    Integer calculateLineCount(ParserRuleContext context) {
        return context.stop.getLine() - context.start.getLine();
    }


}
