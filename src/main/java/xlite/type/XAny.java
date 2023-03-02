package xlite.type;

import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XAny implements XType, HaveValue {
    private XType value;

    public XAny() {}

    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }

    @Override
    public String name() {
        return TypeBuilder.TYPE_ANY;
    }

    @Override
    public XType getValue() {
        return value;
    }

    public void setValue(XType value) {
        this.value = value;
    }
}
