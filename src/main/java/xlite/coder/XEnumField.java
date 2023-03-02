package xlite.coder;

import xlite.type.XType;

public class XEnumField extends XField {
    private String value;

    public XEnumField(String name, XType type, XCoder parent) {
        super(name, type, parent);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
