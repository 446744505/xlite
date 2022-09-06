package xlite.gen;

import lombok.Getter;
import lombok.Setter;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.XLanguage;

import java.io.File;

public class GenContext implements Printer {
    @Getter private final File outDir;
    @Getter private final XLanguage language;
    @Getter private final GenConf conf;
    @Setter private Writer classWriter;

    public GenContext(File outDir, XLanguage language, GenConf conf) {
        this.outDir = outDir;
        this.language = language;
        this.conf = conf;
    }

    public void vprintln(LanguageVisitor<String> visitor) {
        println(language.accept(visitor));
    }

    public void vprintln(int tab, LanguageVisitor<String> visitor) {
        println(tab, language.accept(visitor));
    }

    public void vprint(LanguageVisitor<String> visitor) {
        print(language.accept(visitor));
    }

    @Override
    public void print(String txt, String... x) {
        classWriter.print(txt, x);
    }

    @Override
    public void println() {
        classWriter.println();
    }

    @Override
    public void println(String txt, String... x) {
        classWriter.println(txt, x);
    }

    @Override
    public void print(int tab, String txt, String... x) {
        classWriter.print(tab, txt, x);
    }

    @Override
    public void println(int tab) {
        classWriter.println(tab);
    }

    @Override
    public void println(int tab, String txt, String... x) {
        classWriter.println(tab, txt, x);
    }
}
