package xlite.type;

import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XDouble extends TypeBase {
    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }
}
