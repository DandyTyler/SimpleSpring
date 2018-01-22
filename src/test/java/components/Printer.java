package components;


import com.akos.context.annotation.Autowired;

public class Printer {

    @Autowired
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
