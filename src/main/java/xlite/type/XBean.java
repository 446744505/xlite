package xlite.type;

import lombok.Getter;
import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XBean implements XType {
    @Getter private String name;
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
}
