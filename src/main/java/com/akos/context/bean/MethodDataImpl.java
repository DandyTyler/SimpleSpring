package com.akos.context.bean;

import java.lang.reflect.Method;

/**
 * Реализация интерфейса MethodData
 */
public class MethodDataImpl implements MethodData {

    private final Method method;

    public MethodDataImpl(Method method){
        this.method = method;
        this.method.setAccessible(true);
    }

    @Override
    public String getName() {
        return this.method.getName();
    }

    @Override
    public String getDeclaringClassName() {
        return this.method.getDeclaringClass().getName();
    }

    @Override
    public boolean isAnnotated(Class annotationClass) {
       return method.isAnnotationPresent(annotationClass);
    }

    public Method getMethod() {
        return this.method;
    }
}
