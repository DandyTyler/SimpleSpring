package com.akos.context.factories.config;

import com.akos.context.annotation.*;
import com.akos.context.bean.AnnotatedBeanDefinition;
import com.akos.context.bean.BeanDefinition;
import com.akos.context.bean.MethodDataImpl;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.net.URL;

/**
 * Получает классы, отмеченные @Configuration и создает описания бинов на основе методов этих классов,
 * отмеченыых аннотацией @Bean. Если в файле-конфигурация отмечен @ComponentScan сканирует указанные пакеты
 * на наличие компонентов. Если в @ComponentScan не указаны никакие пакеты сканирует пакет в котором лежит данный
 * файл-конфигурация. Не работает с default пакетом.
 */
public class AnnotatedBeanDefinitionReader {

    private Map<String, BeanDefinition> beansDefinitions;
    private PackageClassScanner scanner = new PackageClassScanner();

    public AnnotatedBeanDefinitionReader(Map<String, BeanDefinition> beansDefinitions) {
        this.beansDefinitions = beansDefinitions;
    }

    public void fillBeansDefinitions(Class<?>... annotatedClasses) {
        for (Class annotatedClass : annotatedClasses) {
            if (annotatedClass.isAnnotationPresent(Configuration.class)) {
                if (annotatedClass.isAnnotationPresent(ComponentScan.class)) {
                    try {
                        ComponentScan componentScan = (ComponentScan) annotatedClass.getAnnotation(ComponentScan.class);
                        List<Class> classes;
                        if (componentScan.value().length == 0) {
                            classes = scanner.getClasses(annotatedClass.getPackage().getName());
                        } else
                            classes = scanner.getClasses(componentScan.value());
                        for (Class cl : classes) {
                            if (cl.isAnnotationPresent(Component.class)) {
                                BeanDefinition beanDefinition = new AnnotatedBeanDefinition();
                                if (cl.isAnnotationPresent(Scope.class)) {
                                    beanDefinition.setScope(((Scope) cl.getAnnotation(Scope.class)).scopeName());
                                }
                                beanDefinition.setFactoryMethodData(null);
                                beanDefinition.setBeanClassName(cl.getName());
                                Component componentAnn = (Component) cl.getAnnotation(Component.class);
                                for (String beanName : componentAnn.value()) {
                                    beansDefinitions.put(beanName, beanDefinition);
                                }
                                beansDefinitions.put(cl.getSimpleName(), beanDefinition);
                            }
                        }
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
                for (Method method : annotatedClass.getMethods()) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        BeanDefinition beanDefinition = new AnnotatedBeanDefinition();
                        beanDefinition.setFactoryMethodData(new MethodDataImpl(method));
                        if (method.isAnnotationPresent(Scope.class)) {
                            beanDefinition.setScope(method.getAnnotation(Scope.class).scopeName());
                        }
                        beanDefinition.setBeanClassName(method.getReturnType().getName());
                        beansDefinitions.put(method.getName(), beanDefinition);
                        for (String beanName : method.getAnnotation(Bean.class).value()) {
                            beansDefinitions.put(beanName, beanDefinition);
                        }
                    }
                }
            } else throw new IllegalArgumentException("Class " + annotatedClass + "doesn't present @Configuration");
        }
    }
}
