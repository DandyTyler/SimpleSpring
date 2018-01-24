package components;

import com.akos.context.factories.AnnotationConfigBeanFactory;
import com.akos.context.factories.BeanFactory;

public class Test {
    public static void main(String[] args) {
        BeanFactory context = new AnnotationConfigBeanFactory(Context.class);
        AutowiredConstructorTest p = (AutowiredConstructorTest) context.getBean("componentTest");
        p.print();

        Printer printer = (Printer)context.getBean(Printer.class);
        printer.print();
    }
}
