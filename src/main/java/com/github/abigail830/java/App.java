package com.github.abigail830.java;

import com.github.abigail830.java.model.JNode;
import com.github.abigail830.java.visitor.JavaFileParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;


@Slf4j
public class App {

    List<JNode> nodes = new ArrayList<>();

    public static void main(String[] args) {
        App app = new App();
        try {
            app.analysisDir("./");
        } catch (Exception e) {
            log.warn("{}", e);
        }

    }

    public void analysisDir(String dir) throws IOException {
        Consumer<Path> fileAnalysis = parseJavaFile(new ArrayList<>());

        try (Stream<Path> walk = Files.walk(Paths.get(dir))) {
            walk.forEach(fileAnalysis::accept);
        }
        this.nodes.forEach(node -> log.info("{}", node));

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
