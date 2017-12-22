package com.akos.context.factories;

import com.akos.context.annotations.Bean;
import com.akos.context.annotations.Configuration;
import com.akos.context.bean.AnnotatedBeanDefinition;
import com.akos.context.bean.BeanDefinition;
import com.akos.context.bean.MethodData;
import com.akos.context.bean.MethodDataImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Создает бины из на основе классов, отмеченных аннотацией @Configuration. Пока что у бина может быть всего одно имя
 * равное имени метода, производящего этот бин.
 */
public class AnnotationConfigBeanFactory implements BeanFactory {

    private Map<String, Object> beans = new HashMap<>();

    private Map<String, BeanDefinition> beansDefinitions = new HashMap<>();

    public AnnotationConfigBeanFactory(Class<?>... annotatedClasses) {
        fillBeansDefinitions(annotatedClasses);
    }

    /**
     * Получает классы, отмеченные @Configuration и создает описания бинов на основе методов этих классов,
     * отмеченыых аннотацией @Bean
     * @param annotatedClasses классы, отмеченные @Configuration
     */
    private void fillBeansDefinitions(Class<?>... annotatedClasses) {
        // TODO: 22.12.2017 Возможно стоит вынести методы для заполнения BeansDefinitions в отдельный класс
        for (Class annotatedClass : annotatedClasses) {
            if (annotatedClass.isAnnotationPresent(Configuration.class)) {
                for (Method method : annotatedClass.getMethods()) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        BeanDefinition beanDefinition = new AnnotatedBeanDefinition();
                        beanDefinition.setFactoryMethodData(new MethodDataImpl(method));
                        Bean beanAnnotation = method.getAnnotation(Bean.class);
                        beanDefinition.setScope(beanAnnotation.scope());
                        beansDefinitions.put(method.getName(), beanDefinition);
                    }
                }
            } else throw new IllegalArgumentException("Class " + annotatedClass + "doesn't present @Configuration");
        }
    }
    /**
     * Создает бины из описания вызвая фабричный метод
     * @param beanName
     */
    private void createBean(String beanName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (beansDefinitions.containsKey(beanName)) {
            MethodData factoryMethodData = beansDefinitions.get(beanName).getFactoryMethodData();
            Method method = factoryMethodData.getMethod();
            Object bean = method.invoke(Class.forName(factoryMethodData.getDeclaringClassName()).newInstance(), null);
            beans.put(beanName, bean);
        } else
            throw new IllegalArgumentException(beanName + " doesn't exist");
    }

    /**
     * Получаем бин по имени. Если бин уже был создан, то возвращаем его иначе пытаемся создать
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
// TODO: 22.12.2017 реализовать работу со scope  обработку исключений
        if (!beans.containsKey(beanName)) {
            try {
                createBean(beanName);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return beans.get(beanName);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return null;
    }
}
