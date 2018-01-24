package com.akos.context.annotation.processor;

import com.akos.context.annotation.Autowired;
import com.akos.context.annotation.Qualifier;
import com.akos.context.exception.AutowireException;
import com.akos.context.factories.BeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Обработка аннотоации @Autowired. Пока только для полей
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory factory;

    public AutowiredAnnotationBeanPostProcessor(BeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object postProcess(Object bean, String beanName) {
        injectFields(bean);
        injectMethods(bean);
        return bean;
    }

    /**
     * Берем тип полей, отмеченных аннотацией Autowired, пытаемся получить бин такого типа из фабрики и установить его
     * в поле. Если присутсвует аннотация Qualifier получаем бин по имени.
     *
     * @param bean
     * @return
     */
    private Object injectFields(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                try {
                    if (field.isAnnotationPresent(Qualifier.class)) {
                        field.set(bean, factory.getBean(field.getAnnotation(Qualifier.class).value()));
                        return bean;
                    }
                    field.set(bean, factory.getBean(field.getType()));
                } catch (IllegalAccessException e) {
                    throw new AutowireException("Failed to inject autowired dependencies, caused by " + e, e);
                }
            }
        }
        return bean;
    }

    private Object injectMethods(Object bean) {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowired.class)) {
                method.setAccessible(true);
                Class[] paramTypes = method.getParameterTypes();
                List<Object> methodArgs = new ArrayList<>();
                for (Class cl : paramTypes) {
                    methodArgs.add(factory.getBean(cl));
                }
                try {
                    method.invoke(bean, methodArgs.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new AutowireException("Failed to inject autowired dependencies, caused by " + e, e);
                }
            }
        }
        return bean;
    }
}
