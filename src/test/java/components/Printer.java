package components;


import com.akos.context.annotation.Autowired;

public class Printer {

    private PrintableString string;

    public Printer() {
    }

    public Printer(PrintableString string) {
        this.string = string;
    }

    @Autowired
    public void setString(PrintableString string) {
        this.string = string;
    }

    public void print() {
        System.out.println(string.getValue());
    }
}
