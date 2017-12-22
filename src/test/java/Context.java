import com.akos.context.annotations.Bean;
import com.akos.context.annotations.Configuration;

@Configuration
public class Context {
    @Bean
    public String helloString(){
        return "Hello";
    }

    @Bean
    public Printer printer1(){
        return new Printer(helloString());
    }
}
