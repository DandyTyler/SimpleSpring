package com.akos.context.bean;

import java.lang.reflect.Method;

/**
 * Класс, содержащий информаию о методах, порождающих бины
 */
public interface MethodData {

    String getName();

    String getDeclaringClassName();

    boolean isAnnotated(Class<?> annotationClass);

    Method getMethod();
}
