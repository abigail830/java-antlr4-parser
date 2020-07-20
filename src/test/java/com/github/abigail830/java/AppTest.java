package com.github.abigail830.java;

import com.github.abigail830.java.model.JNode;
import com.github.abigail830.java.util.JsonUtil;
import com.github.abigail830.java.visitor.JavaFileParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@Slf4j
public class AppTest {

    List<JNode> nodes = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Consumer<Path> fileAnalysis = parseJavaFile(new ArrayList<>());

        try (Stream<Path> walk = Files.walk(Paths.get("./src/test/java/com/github/abigail830/java/sample/ExampleImpl.java"))) {
            walk.forEach(fileAnalysis::accept);
        }
//        this.nodes.forEach(node -> log.info("{}", JsonUtil.toJson(node)));
    }

    @Test
    public void should_match_example() throws IOException {
        String expect = new String(Files.readAllBytes(Paths.get("./src/test/resources/expect.json")))
                .replace("\n", "")
                .replace("\t", "")
                .replace(" ", "");

        final List<String> exampleImpl = this.nodes.stream()
                .map(jNode -> JsonUtil.toJson(jNode))
                .filter(nodeJson -> nodeJson.contains("ExampleImpl"))
                .collect(Collectors.toList());
        assertEquals(exampleImpl.size(), 1);
        System.out.println(exampleImpl.get(0));
        assertEquals(expect, exampleImpl.get(0));

    }

    private Consumer<Path> parseJavaFile(List<JNode> jNodeList) {
        return (Path path) -> {
            try {
                if (!path.toString().endsWith("Test.java") && path.toString().endsWith(".java")) {
                    final JavaFileParser javaFileParser = new JavaFileParser(jNodeList);
                    nodes.add(javaFileParser.parse(path));
                }
            } catch (IOException e) {
                log.warn("Skip {} given: {}", path, e.getMessage());
            }
        };
    }

}
