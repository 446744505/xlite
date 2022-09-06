package xlite.type;

import lombok.Getter;
import lombok.Setter;
import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XList implements XType {
    @Getter @Setter private XType value;

    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }

    @Override
    public boolean isBase() {
        return false;
    }
}
