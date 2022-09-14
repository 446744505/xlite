package xlite.gen;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.language.XLanguage;

public interface GenConf {
    boolean genSetter(XClass clazz, XLanguage language);
    boolean genSetter(XClass clazz, XField field, XLanguage language);
    boolean genMethod(XClass clazz, XMethod method, XLanguage language);
    boolean checkLoopDepend();
}
