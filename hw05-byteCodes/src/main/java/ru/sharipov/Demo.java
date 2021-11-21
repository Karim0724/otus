package ru.sharipov;

import ru.sharipov.ioc.Ioc;

public class Demo {
    public static void main(String[] args) {
        ITestLogging testLogging = Ioc.createTestLogging();
        testLogging.calculation("Karim", 2);
    }
}
