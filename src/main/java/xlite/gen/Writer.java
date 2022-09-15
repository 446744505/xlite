package xlite.gen;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Objects;

public class Writer implements Closeable, Printer {
    private PrintWriter writer;
    private StringBuilder sb;

    public Writer() {
        sb = new StringBuilder();
    }

    public Writer(File file) throws FileNotFoundException {
        writer = new PrintWriter(file);
    }

    @Override
    public void print(String txt, String... x) {
        print0(txt);
        for (String s : x) {
            print0(s);
        }
    }

    @Override
    public void println() {
        println0();
    }

    @Override
    public void println(String txt, String... x) {
        print0(txt);
        for (String s : x) {
            print0(s);
        }
        println0();
    }

    @Override
    public void print(int tab, String txt, String... x) {
        for (int i = 0; i < tab * 4; i++) {
            print(" ");
        }
        print(txt, x);
    }

    @Override
    public void println(int tab) {
        for (int i = 0; i < tab * 4; i++) {
            print(" ");
        }
        println();
    }

    @Override
    public void println(int tab, String txt, String... x) {
        for (int i = 0; i < tab * 4; i++) {
            print(" ");
        }
        println(txt, x);
    }

    private void print0(String txt) {
        if (!Objects.isNull(writer)) {
            writer.print(txt);
        } else {
            sb.append(txt);
        }
    }

    private void println0(String txt) {
        if (!Objects.isNull(writer)) {
            writer.println(txt);
        } else {
            sb.append(txt).append("\n");
        }
    }

    private void println0() {
        if (!Objects.isNull(writer)) {
            writer.println();
        } else {
            sb.append("\n");
        }
    }

    @Override
    public void close() {
        if (!Objects.isNull(writer)) {
            writer.flush();
            writer.close();
        }
    }

    public void deleteEnd(int num) {
        if (!Objects.isNull(sb) && sb.length() >= num) {
            sb.delete(sb.length() - num, sb.length());
        }
    }

    public String getString() {
        if (Objects.isNull(sb)) {
            throw new UnsupportedOperationException();
        }
        return sb.toString();
    }

}
