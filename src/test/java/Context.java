import com.akos.context.annotation.Bean;
import com.akos.context.annotation.Configuration;
import com.akos.context.annotation.Scope;
import annotations.InjectIntAnnotationBeanPostProcessor;

@Configuration
public class Context {

    @Bean
    public InjectIntAnnotationBeanPostProcessor intProcessor(){
        return new InjectIntAnnotationBeanPostProcessor();
    }

    @Bean
    public PrintableString helloString(){
        return new PrintableString("Hello!");
    }

    @Bean
    @Scope(scopeName = "prototype")
    public Printer printer1() {
        return new Printer();
    }
}
