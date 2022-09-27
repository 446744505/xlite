package xlite.type;

import lombok.Getter;
import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

import java.util.Objects;

public class XEnum extends XBean {
    @Getter private final TypeBase inner;//所有字段的类型相同时的类型

    public XEnum(String name, TypeBase inner) {
        super(name);
        this.inner = inner;
    }

    @Override
    public <T> T accept(TypeVisitor<T> visitor, XLanguage language) {
        return visitor.visit(language, this);
    }

    public void assertInner() {
        if (Objects.isNull(inner)) {
            throw new IllegalStateException("if enum as a var`type, it all fields must same type at " + name());
        }
    }
}
