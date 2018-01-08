import com.akos.context.annotation.Autowired;
import com.akos.context.annotation.Qualifier;

public class Printer {

    @Autowired
    @Qualifier("helloStringV2")
    PrintableString string;

    public Printer() {
    }

    public Printer(PrintableString string) {
        this.string = string;
    }

    public void print() {
        System.out.println(string.getValue());
    }
}
