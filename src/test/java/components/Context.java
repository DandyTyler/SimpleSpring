package components;

import com.akos.context.annotation.Bean;
import com.akos.context.annotation.ComponentScan;
import com.akos.context.annotation.Configuration;
import com.akos.context.annotation.Scope;

@Configuration
@ComponentScan("components")
public class Context {

//    @Bean
//    @Scope(scopeName = "prototype")
//    public PrintableString helloString(){
//        return new PrintableString("Hello! "+ System.nanoTime());
//    }

    @Bean({"bean1","bean2"})
    public PrintableString2Version helloStringV2(){
        return new PrintableString2Version("Its works! "+ System.nanoTime());
    }

    @Bean
    @Scope(scopeName = "prototype")
    public Printer printer1() {
        return new Printer();
    }
}
