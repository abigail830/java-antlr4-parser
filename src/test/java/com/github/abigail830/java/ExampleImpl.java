package com.github.abigail830.java;

import lombok.extern.slf4j.Slf4j;

interface Example {
    String getName();
}

@Slf4j
public class ExampleImpl implements Example {

    String name;

    public static void main(String[] args) {
        log.info("Hello World");
    }

    @Override
    public String getName() {
        return name;
    }
}

