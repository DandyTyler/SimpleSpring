package com.akos.context.factories;

import com.akos.context.bean.BeanDefinition;
import com.akos.context.bean.MethodData;
import com.akos.context.factories.config.AnnotatedBeanDefinitionReader;
import com.akos.context.factories.config.BeanPostProcessor;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: 02.01.2018 Добавить сканнер компонентов

/**
 * Создает бины из на основе классов, отмеченных аннотацией @Configuration. Пока что у бина может быть всего одно имя
 * равное имени метода, производящего этот бин.
 */
public class AnnotationConfigBeanFactory implements BeanFactory {

    private Map<String, Object> beans = new HashMap<>();

    private Map<String, BeanDefinition> beansDefinitions = new HashMap<>();

    private AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beansDefinitions);

    public AnnotationConfigBeanFactory(Class<?>... annotatedClasses) {
        reader.fillBeansDefinitions(annotatedClasses);
        createBeans();
        doPostProcessors();
    }

    /**
     * Создает бины из описания вызвая фабричный метод
     *
     * @param beanName
     */
    private void createBean(String beanName) {
        // TODO: 02.01.2018 Сделать нормальную обработку исключений
        if (beansDefinitions.containsKey(beanName)) {
            try {
                MethodData factoryMethodData = beansDefinitions.get(beanName).getFactoryMethodData();
                Method method = factoryMethodData.getMethod();
                Object bean = method.invoke(Class.forName(factoryMethodData.getDeclaringClassName()).newInstance(), null);
                beans.put(beanName, bean);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else
            throw new IllegalArgumentException(beanName + " doesn't exist");
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
        List<BeanPostProcessor> postProcessors = new ArrayList<>();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            doPostProcessors(entry.getValue(), entry.getKey());
        }
    }

    // TODO: 02.01.2018 Хранить все постпроцесоры отдельно от остальных бинов, чтобы избежать постоянного вызова instanceof

    /**
     * Применяет постпроцессоры к конкретному бину
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
     * Получаем бин по имени. Если бин уже был создан, то возвращаем его иначе пытаемся создать
     *
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        if (!beans.containsKey(beanName) | beansDefinitions.get(beanName).getScope().equals("prototype")) {
            createBean(beanName);
            doPostProcessors(beans.get(beanName), beanName);
        }
        return beans.get(beanName);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return null;
    }
}
