package xlite.gen;

import xlite.coder.GenCoder;
import xlite.language.XLanguage;
import xlite.util.Util;

import java.io.File;

public class XGenerator {
    private final File srcOutDir;
    private final XLanguage language;
    private final GenConf conf;

    public XGenerator(String srcOut, String language, XGenFactory factory) {
        this.srcOutDir = new File(srcOut);
        this.conf = factory.createConf();
        this.language = factory.createLanguage(language);
    }

    public void gen(GenCoder code) {
        srcOutDir.mkdirs();
        Util.cleanDir(srcOutDir);
        GenContext context = new GenContext(srcOutDir, language, conf);
        code.gen(context);
    }
}
