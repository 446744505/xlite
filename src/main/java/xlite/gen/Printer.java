package xlite.gen;

public interface Printer {
    void print(String txt, String... x);
    void println();
    void println(String txt, String... x);

    void print(int tab, String txt, String... x);
    void println(int tab);
    void println(int tab, String txt, String... x);
}
