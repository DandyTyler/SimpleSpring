import com.akos.context.annotation.Bean;
import com.akos.context.annotation.Configuration;
import com.akos.context.annotation.Scope;
import annotations.InjectIntAnnotationBeanPostProcessor;
import com.akos.context.annotation.processor.AutowiredAnnotationBeanPostProcessor;

import java.util.Date;

@Configuration
public class Context {

    @Bean
    @Scope(scopeName = "prototype")
    public PrintableString helloString(){
        return new PrintableString("Hello! "+ System.nanoTime());
    }

    @Bean
    public PrintableString2Version helloStringV2(){
        return new PrintableString2Version("Hello! "+ System.nanoTime());
    }

    @Bean
    @Scope(scopeName = "prototype")
    public Printer printer1() {
        return new Printer();
    }
}
