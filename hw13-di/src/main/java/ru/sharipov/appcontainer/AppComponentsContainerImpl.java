package ru.sharipov.appcontainer;


import ru.sharipov.appcontainer.api.AppComponent;
import ru.sharipov.appcontainer.api.AppComponentsContainer;
import ru.sharipov.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        checkConfigClass(configClass);
        var instance = configClass.getDeclaredConstructor().newInstance();
        var sortedByOrderMethods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(methodByOrderComparator())
                .collect(Collectors.toList());
        for (Method method : sortedByOrderMethods) {
            var annotation = method.getAnnotation(AppComponent.class);
            var name = annotation.name();
            var parametersToInject = new ArrayList<>();
            Arrays.stream(method.getParameters())
                    .forEach(parameter -> parametersToInject.add(getAppComponent(parameter.getType())));
            Object service = method.invoke(instance, parametersToInject.toArray());
            appComponents.add(service);
            appComponentsByName.put(name, service);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        C result = null;
        for (Object component: appComponents) {
            if (component.getClass().equals(componentClass)) {
                result = (C) component;
                break;
            }
            if (componentClass.isAssignableFrom(component.getClass())) {
                result = (C) component;
                break;
            }
        }
        if (result == null) {
            throw new NoSuchElementException("No component with class: " + componentClass.getSimpleName());
        }
        return result;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private Comparator<Method> methodByOrderComparator() {
        return (o1, o2) -> {
            var o1Annotation = o1.getAnnotation(AppComponent.class);
            var o2Annotation = o2.getAnnotation(AppComponent.class);
            return Integer.compare(o1Annotation.order(), o2Annotation.order());
        };
    }
}
