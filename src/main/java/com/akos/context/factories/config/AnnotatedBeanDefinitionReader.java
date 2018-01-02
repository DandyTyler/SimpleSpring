package com.akos.context.factories.config;

import com.akos.context.annotation.Bean;
import com.akos.context.annotation.Configuration;
import com.akos.context.annotation.Scope;
import com.akos.context.bean.AnnotatedBeanDefinition;
import com.akos.context.bean.BeanDefinition;
import com.akos.context.bean.MethodDataImpl;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Получает классы, отмеченные @Configuration и создает описания бинов на основе методов этих классов,
 * отмеченыых аннотацией @Bean
 */
public class AnnotatedBeanDefinitionReader {
    private Map<String, BeanDefinition> beansDefinitions;

    public AnnotatedBeanDefinitionReader(Map<String, BeanDefinition> beansDefinitions) {
        this.beansDefinitions = beansDefinitions;
    }

    public void fillBeansDefinitions(Class<?>... annotatedClasses) {
        for (Class annotatedClass : annotatedClasses) {
            if (annotatedClass.isAnnotationPresent(Configuration.class)) {
                for (Method method : annotatedClass.getMethods()) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        BeanDefinition beanDefinition = new AnnotatedBeanDefinition();
                        beanDefinition.setFactoryMethodData(new MethodDataImpl(method));
                        if (method.isAnnotationPresent(Scope.class)) {
                            beanDefinition.setScope(method.getAnnotation(Scope.class).scopeName());
                        }
                        beanDefinition.setBeanClassName(method.getReturnType().getName());
                        beansDefinitions.put(method.getName(), beanDefinition);
                    }
                }
            } else throw new IllegalArgumentException("Class " + annotatedClass + "doesn't present @Configuration");
        }
    }
}
