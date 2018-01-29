package com.akos.context.factories;

import com.akos.context.annotation.Autowired;
import com.akos.context.annotation.processor.AutowiredAnnotationBeanPostProcessor;
import com.akos.context.bean.BeanDefinition;
import com.akos.context.bean.MethodData;
import com.akos.context.exception.BeanCreationException;
import com.akos.context.factories.config.AnnotatedBeanDefinitionReader;
import com.akos.context.annotation.processor.BeanPostProcessor;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Создает бины из на основе классов, отмеченных аннотацией @Configuration. Пока что у бина может быть всего одно имя
 * равное имени метода, производящего этот бин.
 */
public class AnnotationConfigBeanFactory implements BeanFactory {

    private Map<String, Object> beans = new HashMap<>();

    private Map<String, BeanDefinition> beansDefinitions = new HashMap<>();

    private AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beansDefinitions);

    {
        beans.put("autowiredAnnotationBeanPostProcessor", new AutowiredAnnotationBeanPostProcessor(this));
    }

    public AnnotationConfigBeanFactory(Class<?>... annotatedClasses) {
        registerBeans(annotatedClasses);
    }

    /**
     * Создает бины из описания вызвая фабричный метод либо конструктор
     *
     * @param beanName имя бина
     */
    private void createBean(String beanName) {
        if (beansDefinitions.containsKey(beanName)) {
            try {
                MethodData factoryMethodData = beansDefinitions.get(beanName).getFactoryMethodData();
                if (factoryMethodData != null) {
                    Method method = factoryMethodData.getMethod();
                    method.setAccessible(true);
                    Object bean = method.invoke(Class.forName(factoryMethodData.getDeclaringClassName()).newInstance());
                    beans.put(beanName, bean);
                } else {
                    Class beanClass = Class.forName(beansDefinitions.get(beanName).getBeanClassName());
                    Constructor[] constructors = beanClass.getDeclaredConstructors();
                    List<Constructor> autowiredAnnotatedConstructors = new ArrayList<>();
                    for (Constructor constructor : constructors) {
                        if (constructor.isAnnotationPresent(Autowired.class)) {
                            constructor.setAccessible(true);
                            autowiredAnnotatedConstructors.add(constructor);
                        }
                    }
                    if (autowiredAnnotatedConstructors.size() == 0) {
                        try {
                            beans.put(beanName, beanClass.newInstance());
                        } catch (InstantiationException e) {
                            throw new BeanCreationException("No default constructor", e);
                        }
                    } else {
                        if (autowiredAnnotatedConstructors.size() > 1)
                            throw new BeanCreationException("Bean can't have more than 1 autowired constructor");
                        Class[] paramTypes = autowiredAnnotatedConstructors.get(0).getParameterTypes();
                        ArrayList<Object> autowiredArguments = new ArrayList<>();
                        for (Class cl : paramTypes) {
                            autowiredArguments.add(getBean(cl));
                        }
                        Object bean = autowiredAnnotatedConstructors.get(0).newInstance(autowiredArguments.toArray());
                        beans.put(beanName, bean);
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new BeanCreationException(e);
            }
        } else
            throw new IllegalArgumentException("Bean with the name \"" + beanName + "\" doesn't exist");
    }

    private void createBeans() {
        for (Map.Entry<String, BeanDefinition> entry : beansDefinitions.entrySet()) {
            createBean(entry.getKey());
        }
    }

    /**
     * Применяет постпроцессоры ко всем бинам
     */
    private void doPostProcessors() {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            doPostProcessors(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Находим бины, которые являются постпроцессороами и применяем их к переданному бину
     */
    private void doPostProcessors(Object bean, String beanName) {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            if (entry.getValue() instanceof BeanPostProcessor) {
                BeanPostProcessor postProcessor = (BeanPostProcessor) entry.getValue();
                bean = postProcessor.postProcess(bean, beanName);
            }
        }
    }

    /**
     * Позволяет регистрировать новые бины из класса-конфигурации.
     *
     * @param annotatedClasses Класс-конфигурация
     */
    public void registerBeans(Class<?>... annotatedClasses) {
        reader.fillBeansDefinitions(annotatedClasses);
//        createBeans();
//        doPostProcessors();
    }

    /**
     * Получаем бин по имени. Если бин уже был создан, то возвращаем его иначе пытаемся создать. Если prototype то создаем
     * новый всегда
     *
     * @param beanName Имя бина
     * @return Экземпляр бина
     */
    @Override
    public synchronized Object getBean(String beanName) {
        if (beans.containsKey(beanName)) {
            if (beansDefinitions.get(beanName).getScope().equals("prototype")) {
                createBean(beanName);
                doPostProcessors(beans.get(beanName), beanName);
            }
            return beans.get(beanName);
        }
        createBean(beanName);
        doPostProcessors(beans.get(beanName), beanName);
        return beans.get(beanName);
    }

    /**
     * Получение бина по классу. Если не существует бинов такого класса и его наследников или таких бинов несколько
     * кидается исключение.Один бин может лежать под разными именами
     *
     * @param clazz Класс
     * @return Экземпляр бина
     */
    @Override
    public <T> T getBean(Class<T> clazz) {
        Map.Entry<String, BeanDefinition> candidate = null;
        for (Map.Entry<String, BeanDefinition> entry : beansDefinitions.entrySet()) {
            try {
                if (candidate == null) {
                    if (clazz.isAssignableFrom(Class.forName(entry.getValue().getBeanClassName())))
                        candidate = entry;
                } else if (!candidate.getValue().equals(entry.getValue()) && clazz.isAssignableFrom(Class.forName(entry.getValue().getBeanClassName()))) {
                    throw new IllegalArgumentException("There are several beans for " + clazz.getName() + " class");
                }
            } catch (ClassNotFoundException e) {
                throw new BeanCreationException(e);
            }
        }
        if (candidate == null)
            throw new IllegalArgumentException("Bean for class " + clazz.getName() + " doesn't exist");
        return (T) getBean(candidate.getKey());
    }
}
