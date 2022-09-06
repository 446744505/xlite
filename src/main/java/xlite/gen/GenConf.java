package xlite.gen;

import xlite.coder.XClass;
import xlite.coder.XField;

public interface GenConf {
    boolean genSetter(XClass clazz);
    boolean genSetter(XField field);
    boolean checkLoopDepend();
}
