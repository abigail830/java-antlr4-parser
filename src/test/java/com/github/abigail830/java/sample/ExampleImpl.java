package com.github.abigail830.java.sample;

import lombok.extern.slf4j.Slf4j;

interface Example {
    public String name = "Hello";
    Integer init = 0;

    String getName();
}

@Slf4j
public class ExampleImpl implements Example {

    private String name;

    public static void main(String[] args) {
        log.info("Hello World");
    }

    @Override
    public String getName() {
        return name;
    }
}

