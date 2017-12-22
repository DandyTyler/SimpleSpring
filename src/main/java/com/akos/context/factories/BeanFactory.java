package com.akos.context.factories;

public interface BeanFactory {

    public Object getBean(String name);

    public <T> T getBean(String name, Class<T> clazz);
}
