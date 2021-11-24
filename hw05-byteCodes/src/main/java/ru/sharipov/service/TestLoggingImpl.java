package ru.sharipov.service;

import ru.sharipov.ioc.log.Log;

public class TestLoggingImpl implements TestLogging {
    @Override
    public void calculation(int a) {
        System.out.println("int a");
    }

    @Override
    public void calculation(int a, int b) {
        System.out.println("int a, int b");
    }

    @Override
    @Log
    public void calculation(int a, int b, int c) {
        System.out.println("int a, int b, int c");
    }

    @Override
    public void calculation(int b, String d) {
        System.out.println("int b, String d");
    }

    @Override
    @Log
    public void calculation(String b, int d) {
        System.out.println("String b, int d");
    }

    @Override
    @Log
    public void calculation(long a) {
        System.out.println("long a");
    }

    @Override
    @Log
    public void calculation() {
        System.out.println("no params");
    }

    public void doSomething() {
        System.out.println("Do smth");
    }
}
