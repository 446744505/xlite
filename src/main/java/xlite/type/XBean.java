package xlite.type;

import lombok.Getter;
import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;
import xlite.util.Util;

public class XBean implements XType {
    private String name;
    @Getter private Class clazz;

    public XBean(String name) {
        this.name = name;
    }

    public XBean(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }

    @Override
    public boolean isBase() {
        return false;
    }

    @Override
    public String name() {
        if (Util.notEmpty(name)) {
            return name;
        }
        return clazz.getSimpleName();
    }
}
