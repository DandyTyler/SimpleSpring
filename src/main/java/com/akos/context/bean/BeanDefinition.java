package com.akos.context.bean;

/**
 * Содержит свойства бинов, такие как Scope, типы аргументов конструктора и тд.
 */
public interface BeanDefinition {

    void setScope(String scope);

    String getScope();

    MethodData getFactoryMethodData();

    void setFactoryMethodData(MethodData factoryMethodData);
}
