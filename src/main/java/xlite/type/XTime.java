package xlite.type;

import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XTime implements XType {
    XTime() {}

    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }

    @Override
    public String name() {
        return TypeBuilder.TYPE_TIME;
    }
}
