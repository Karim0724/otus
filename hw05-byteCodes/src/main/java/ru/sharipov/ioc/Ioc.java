package ru.sharipov.ioc;

import ru.sharipov.service.TestLogging;
import ru.sharipov.ioc.log.LogInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Ioc {
    private Ioc() {

    }

    public static TestLogging createTestLoggingProxy(TestLogging testLogging) {
        InvocationHandler handler = new LogInvocationHandler(testLogging);
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }
}
