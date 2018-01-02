import com.akos.context.annotation.Autowired;
import annotations.InjectInt;

public class Printer {

    // TODO: 02.01.2018  Реализовать autowired
    @Autowired
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
