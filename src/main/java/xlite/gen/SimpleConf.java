package xlite.gen;

import xlite.coder.XClass;
import xlite.coder.XField;

public class SimpleConf implements GenConf {
    @Override
    public boolean genSetter(XClass clazz) {
        return true;
    }

    @Override
    public boolean genSetter(XField field) {
        return true;
    }

    @Override
    public boolean checkLoopDepend() {
        return false;
    }
}
