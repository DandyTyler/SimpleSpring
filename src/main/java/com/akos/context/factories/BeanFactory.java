package com.akos.context.factories;

public interface BeanFactory {

    Object getBean(String name);

    <T> T getBean(Class<T> clazz);
}
