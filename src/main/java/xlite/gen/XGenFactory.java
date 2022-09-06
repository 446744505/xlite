package xlite.gen;

import xlite.language.XLanguage;

public interface XGenFactory {
    XLanguage createLanguage(String l);
    GenConf createConf();
}
