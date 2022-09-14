package xlite.gen;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.language.XLanguage;

public class SimpleConf implements GenConf {
    @Override
    public boolean genSetter(XClass clazz, XLanguage language) {
        return true;
    }

    @Override
    public boolean genSetter(XClass clazz, XField field, XLanguage language) {
        return true;
    }

    @Override
    public boolean genMethod(XClass clazz, XMethod method, XLanguage language) {
        return true;
    }

    @Override
    public boolean checkLoopDepend() {
        return false;
    }
}
