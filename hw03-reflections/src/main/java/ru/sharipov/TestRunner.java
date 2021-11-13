package ru.sharipov;

import ru.sharipov.annotation.After;
import ru.sharipov.annotation.Before;
import ru.sharipov.annotation.Test;
import ru.sharipov.exception.AfterInvocationException;
import ru.sharipov.exception.BeforeInvocationException;
import ru.sharipov.exception.ClassInstantiationException;
import ru.sharipov.util.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    private final Class<?> clazz;
    private final List<Method> beforeMethods;
    private final List<Method> afterMethods;
    private final List<Method> testMethods;
    private final List<Method> failed;

    public TestRunner(Class<?> clazz) {
        beforeMethods = new ArrayList<>();
        afterMethods = new ArrayList<>();
        testMethods = new ArrayList<>();
        failed = new ArrayList<>();
        this.clazz = clazz;
        setUpMethods();
    }

    private void setUpMethods() {
        var methods = clazz.getMethods();
        Arrays.stream(methods).forEach(method -> {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        });

    }

    public void run() {
        testMethods.forEach(this::runTest);
        printStatistics();
    }

    private Object getInstance() {
        try {
            return ReflectionHelper.instantiate(clazz);
        } catch (Exception e) {
            throw new ClassInstantiationException("Can't instantiate class. No default constructor", e);
        }
    }

    private void printStatistics() {
        System.out.println("\n-----------\n");
        System.out.println("Succeded: " + (long) (testMethods.size() - failed.size()) + "\nFailed: " + failed.size() + "\nAll: " + testMethods.size());
    }

    private void runTest(Method method) {
        var instance = getInstance();
        try {
            runBefore(instance);
            executeTestMethod(method, instance);
        } finally {
            runAfter(instance);
        }
    }

    private void executeTestMethod(Method method, Object instance) {
        try {
            method.invoke(instance);
        } catch (Exception e) {
            failed.add(method);
        }
    }

    private void runBefore(Object instance) {
        for (Method before: beforeMethods) {
            try {
                before.invoke(instance);
            } catch (Exception e) {
                throw new BeforeInvocationException("Before method " + before.getName() + " has thrown exception: " + e.getClass(), e);
            }
        }
    }

    private void runAfter(Object instance) {
        for (Method after: afterMethods) {
            try {
                after.invoke(instance);
            } catch (Exception e) {
                throw new AfterInvocationException("After method " + after.getName() + " has thrown exception: " + e.getMessage(), e);
            }
        }
    }
}
