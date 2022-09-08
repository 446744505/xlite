package xlite.conf;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.gen.GenConf;

public class ConfGenConf implements GenConf {
    @Override
    public boolean genSetter(XClass clazz) {
        return false;
    }

    @Override
    public boolean genSetter(XField field) {
        return field.getType().isBase();
    }

    @Override
    public boolean checkLoopDepend() {
        return true;
    }
}
