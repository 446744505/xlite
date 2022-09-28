package xlite.type;

import lombok.Getter;
import lombok.Setter;
import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public class XAny implements XType, HaveValue {
    @Getter @Setter private XType value;

    public XAny() {}

    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }

    @Override
    public String name() {
        return TypeBuilder.TYPE_ANY;
    }
}
