package com.akos.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Данной аннотацией отмечаются поля, методы или конструкторы, в которые необходимо внедрить зависимости.
 */
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
