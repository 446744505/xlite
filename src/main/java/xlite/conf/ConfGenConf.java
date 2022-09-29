package xlite.conf;

import xlite.coder.XClass;
import xlite.coder.XEnumer;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.GenConf;
import xlite.language.XLanguage;
import xlite.type.TypeBase;

public class ConfGenConf implements GenConf {
    @Override
    public boolean genSetter(XClass clazz, XLanguage language) {
        return false;
    }

    @Override
    public boolean genSetter(XClass clazz, XField field, XLanguage language) {
        return field.getType() instanceof TypeBase;
    }

    @Override
    public boolean genGetter(XClass clazz, XLanguage language) {
        return !(clazz instanceof XEnumer);
    }

    @Override
    public boolean genGetter(XClass clazz, XField field, XLanguage language) {
        return !field.isStaticed();
    }

    @Override
    public boolean genMethod(XClass clazz, XMethod method, XLanguage language) {
        if (clazz instanceof XEnumer) {
            if (method.getName().equals(PrintEnumValueMethod.ENUM_METHOD_VALUE)) {
                return true;
            }
            return false;
        }
        return true;
    }

}
