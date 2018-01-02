package com.akos.context.factories.config;

/**
 * Определяет действия, котрые необходимо выполнить над бином, созданным из BeanDefinition с конструктором по умолчанию.
 * Например для обработки аннотаций.
 */
public interface BeanPostProcessor {
    default Object postProcess(Object bean, String beanName) {
        return bean;
    }
}
