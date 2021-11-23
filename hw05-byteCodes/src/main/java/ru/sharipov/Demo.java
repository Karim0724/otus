package ru.sharipov;

import ru.sharipov.ioc.Ioc;
import ru.sharipov.service.TestLogging;
import ru.sharipov.service.TestLoggingImpl;

public class Demo {
    public static void main(String[] args) {
        TestLogging testLogging = Ioc.createTestLoggingProxy(new TestLoggingImpl());
        testLogging.calculation(2, "Karim");
    }
}
