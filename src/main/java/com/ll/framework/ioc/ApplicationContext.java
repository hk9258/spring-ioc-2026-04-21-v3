package com.ll.framework.ioc;

import com.ll.framework.ioc.annotations.Component;
import com.ll.framework.ioc.annotations.Configuration;
import com.ll.framework.ioc.annotations.Repository;
import com.ll.framework.ioc.annotations.Service;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private final String basePackage;
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
    }

    public void init() {
        Set<Class<?>> componentClasses = scanComponentClasses();

        for (Class<?> clazz : componentClasses) {
            createBean(clazz);
        }
    }

    public <T> T genBean(String beanName) {
        return (T) beans.get(beanName);
    }

    private Set<Class<?>> scanComponentClasses() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> classes = new HashSet<>();
        classes.addAll(reflections.getTypesAnnotatedWith(Component.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Configuration.class));

        classes.removeIf(this::isNotBeanClass);

        return classes;
    }

    private boolean isNotBeanClass(Class<?> clazz) {
        return clazz.isAnnotation()
                || clazz.isInterface()
                || Modifier.isAbstract(clazz.getModifiers());
    }

    private Object createBean(Class<?> clazz) {
        String beanName = getBeanName(clazz);

        if (beans.containsKey(beanName)) {
            return beans.get(beanName);
        }

        Object instance = createInstance(clazz);
        beans.put(beanName, instance);

        return instance;
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);

            Object[] args = createConstructorArgs(constructor);

            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] createConstructorArgs(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = createBean(parameterTypes[i]);
        }

        return args;
    }

    private String getBeanName(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}