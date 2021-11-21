package ru.sharipov.ioc;

import ru.sharipov.ITestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LogInvocationHandler implements InvocationHandler {
    private final ITestLogging testLogging;

    public LogInvocationHandler(ITestLogging testLogging) {
        this.testLogging = testLogging;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method classMethod = findClassMethod(method);
        if (classMethod.isAnnotationPresent(Log.class)) {
            System.out.println("executed method: " + getMethodPresentation(classMethod, args));
        }
        return method.invoke(testLogging, args);
    }

    private String getMethodPresentation(Method method, Object[] args) {
        return method.getName() + "(" +
                Arrays.stream(args)
                        .map(Object::toString)
                        .collect(Collectors.joining(",")) +
                ")";
    }

    private Method findClassMethod(Method interfaceMethod) {
        return Arrays.stream(testLogging.getClass().getMethods())
                .filter(classMethod ->
                        classMethod.getName().equals(interfaceMethod.getName()) &&
                                areParametersTheSame(classMethod.getParameters(), interfaceMethod.getParameters())

                )
                .findFirst().get();
    }

    private boolean areParametersTheSame(Parameter[] classMethodParameters, Parameter[] interfaceMethodParameters) {
        if (interfaceMethodParameters.length != classMethodParameters.length) {
            return false;
        }
        for (int i = 0; i < classMethodParameters.length; i++) {
            if (!classMethodParameters[i].getType().equals(interfaceMethodParameters[i].getType())) {
                return false;
            }
        }
        return true;
    }
}
