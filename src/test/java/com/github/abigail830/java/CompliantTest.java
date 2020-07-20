package com.github.abigail830.java;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class CompliantTest {
    @Test
    public void whole_tree() throws IOException {
        CharStream stream = CharStreams.fromPath(Paths.get("./src/test/java/com/github/abigail830/java/sample/ExampleImpl.java"));
        com.github.abigail830.java.JavaLexer lexer = new com.github.abigail830.java.JavaLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        com.github.abigail830.java.JavaParser parser = new com.github.abigail830.java.JavaParser(tokens);
        ParseTree tree = parser.compilationUnit();
        System.out.println(tree.getText());
    }
}
