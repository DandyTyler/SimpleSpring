package annotations;

import com.akos.context.annotation.processor.BeanPostProcessor;

import java.lang.reflect.Field;
import java.util.Random;

public class InjectIntAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcess(Object bean, String beanName) {
        for (Field field:bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectInt.class)){
                field.setAccessible(true);
                try {
                    field.set(bean,new Random().nextInt(15));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
