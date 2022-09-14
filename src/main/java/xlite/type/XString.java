package xlite.type;


import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XString extends TypeBase {
    XString() {}

    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }
}
