package com.akos.context.factories;

/**
 * Фабрика бинов.
 */
public interface BeanFactory {

    /**
     * Получание бина по имени
     * @param name Имя бина
     * @return Экземпляр бина
     */
    Object getBean(String name);

    /**
     * Получение бина по классу
     * @param clazz Класс
     * @return Экземпляр бина
     */
    <T> T getBean(Class<T> clazz);
}
