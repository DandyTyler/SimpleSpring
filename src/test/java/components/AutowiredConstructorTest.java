package components;

import com.akos.context.annotation.Autowired;
import com.akos.context.annotation.Component;

@Component("componentTest")
public class AutowiredConstructorTest {
    private PrintableString str;

    private Printer printer;

    @Autowired
    public AutowiredConstructorTest(PrintableString str, Printer printer) {
        this.str = str;
        this.printer = printer;
    }

    public void print(){
        printer.setString(str);
        printer.print();
    }
}
