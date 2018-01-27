package com.akos.context.annotation.processor;

/**
 * Определяет действия, котрые необходимо выполнить над бином, созданным из BeanDefinition. Например для обработки аннотаций.
 */
public interface BeanPostProcessor {
    /**
     * Необходимо переопределить этот метод добавив ему нужную функциональность
     * @param bean Экземпляр бина
     * @param beanName Имя бина
     * @return Экземпляр бина после изменения
     */
    default Object postProcess(Object bean, String beanName) {
        return bean;
    }
}
