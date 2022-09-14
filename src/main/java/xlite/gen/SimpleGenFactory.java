package xlite.gen;

import xlite.language.Java;
import xlite.language.XLanguage;

public class SimpleGenFactory implements XGenFactory {
    @Override
    public XLanguage createLanguage(String l) {
        if ("java".equals(l)) {
            return Java.INSTANCE;
        }
        throw new UnsupportedOperationException(String.format("unsupported language %s", l));
    }

    @Override
    public GenConf createConf() {
        return new SimpleConf();
    }
}
