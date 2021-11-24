package ru.sharipov.ioc.log;

import ru.sharipov.service.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LogInvocationHandler implements InvocationHandler {
    private final TestLogging testLogging;
    private final List<Method> classMethodsWithAnnotation;

    public LogInvocationHandler(TestLogging testLogging) {
        this.testLogging = testLogging;
        this.classMethodsWithAnnotation = getAnnotatedMethods();
    }

    private List<Method> getAnnotatedMethods() {
        return Arrays.stream(testLogging.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Log.class))
                .toList();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Optional<Method> classMethodOptional = getAnnotatedClassMethod(method);
        classMethodOptional.ifPresent(classMethod ->
                System.out.println("executed method: " + getMethodPresentation(classMethod, args))
        );
        return method.invoke(testLogging, args);
    }

    private String getMethodPresentation(Method method, Object[] args) {
        if (args == null || args.length == 0) {
            return method.getName() + "()";
        }
        return method.getName() + "(" +
                Arrays.stream(args)
                        .map(Object::toString)
                        .collect(Collectors.joining(",")) +
                ")";
    }

    private Optional<Method> getAnnotatedClassMethod(Method interfaceMethod) {
        return classMethodsWithAnnotation.stream()
                .filter(classMethod ->
                        classMethod.getName().equals(interfaceMethod.getName()) &&
                        Arrays.equals(classMethod.getParameterTypes(), interfaceMethod.getParameterTypes())

                )
                .findFirst();
    }
}
