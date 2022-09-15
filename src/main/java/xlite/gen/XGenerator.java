package xlite.gen;

import xlite.coder.GenCoder;
import xlite.language.XLanguage;
import xlite.util.Util;

import java.io.File;

public class XGenerator {
    private final File outDir;
    private final XLanguage language;
    private final GenConf conf;

    public XGenerator(String out, String language, XGenFactory factory) {
        this.outDir = new File(out);
        this.conf = factory.createConf();
        this.language = factory.createLanguage(language);
    }

    public void gen(GenCoder code) {
        outDir.mkdirs();
        Util.cleanDir(outDir);
        GenContext context = new GenContext(outDir, language, conf);
        code.gen(context);
    }
}
