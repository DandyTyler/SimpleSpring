package components;

import com.akos.context.annotation.Autowired;
import com.akos.context.annotation.Qualifier;

public class Printer {

    PrintableString string;

    public Printer() {
    }

    public Printer(PrintableString string) {
        this.string = string;
    }

    public void setString(PrintableString string) {
        this.string = string;
    }

    public void print() {
        System.out.println(string.getValue());
    }
}
