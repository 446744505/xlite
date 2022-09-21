package xlite.type;

import lombok.Getter;
import lombok.Setter;
import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XAny implements XType {
    @Getter @Setter private TypeBase inner;

    public XAny() {}

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
        return TypeBuilder.TYPE_ANY;
    }
}
