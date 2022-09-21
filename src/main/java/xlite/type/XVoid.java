package xlite.type;

import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XVoid implements XType {
    XVoid() {}
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
        return TypeBuilder.TYPE_VOID;
    }
}
