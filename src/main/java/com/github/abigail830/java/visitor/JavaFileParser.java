package com.github.abigail830.java.visitor;

import com.github.abigail830.java.model.JNode;
import lombok.Getter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Getter
public class JavaFileParser {

    private List<JNode> jNodeList;

    public JavaFileParser(List<JNode> jNodeList) {
        this.jNodeList = jNodeList;
    }

    public JNode parse(Path path) throws IOException {
        CharStream stream = CharStreams.fromPath(path);
        com.github.abigail830.java.JavaLexer lexer = new com.github.abigail830.java.JavaLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        com.github.abigail830.java.JavaParser parser = new com.github.abigail830.java.JavaParser(tokens);

        NodeVisitor nodeVisitor = new NodeVisitor();
        final JNode result = nodeVisitor.visit(parser.compilationUnit());
        return result;
    }
}
