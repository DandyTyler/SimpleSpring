import com.akos.context.factories.AnnotationConfigBeanFactory;
import com.akos.context.factories.BeanFactory;

public class Test {
    public static void main(String[] args) {
        BeanFactory context = new AnnotationConfigBeanFactory(Context.class);
        Printer p = (Printer)context.getBean("printer1");
        p.print();
    }
}
