package com.akos.context.annotation.processor;

import com.akos.context.annotation.Autowired;
import com.akos.context.exception.AutowireException;
import com.akos.context.factories.BeanFactory;

import java.lang.reflect.Field;

/**
 * Обработка аннотоации @Autowired. Пока только для полей
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    BeanFactory factory;

    public AutowiredAnnotationBeanPostProcessor(BeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object postProcess(Object bean, String beanName) {
        return injectFields(bean);
    }

    /**
     * Берем тип полей, отмеченных аннотацией Autowired, пытаемся получить бин такого типа из фабрики и установить его в поле
     * @param bean
     * @return
     */
    private Object injectFields(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                try {
                    field.set(bean, factory.getBean(field.getType()));
                } catch (IllegalAccessException e) {
                    throw new AutowireException(e);
                }
            }
        }
        return bean;
    }
}
