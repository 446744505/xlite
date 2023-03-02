package xlite.type;

import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XList implements XType, HaveValue {
    private XType value;

    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }

    @Override
    public String name() {
        return TypeBuilder.TYPE_LIST;
    }

    @Override
    public XType getValue() {
        return value;
    }

    public void setValue(XType value) {
        this.value = value;
    }
}
