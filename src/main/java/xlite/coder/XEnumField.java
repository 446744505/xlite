package xlite.coder;

import lombok.Getter;
import lombok.Setter;
import xlite.type.XType;

public class XEnumField extends XField {
    @Getter @Setter private String value;

    public XEnumField(String name, XType type, XCoder parent) {
        super(name, type, parent);
    }
}
