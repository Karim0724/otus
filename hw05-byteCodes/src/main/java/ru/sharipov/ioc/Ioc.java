package ru.sharipov.ioc;

import ru.sharipov.ITestLogging;
import ru.sharipov.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Ioc {
    private Ioc() {

    }

    public static ITestLogging createTestLogging() {
        InvocationHandler handler = new LogInvocationHandler(new TestLogging());
        return (ITestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{ITestLogging.class}, handler);
    }
}
