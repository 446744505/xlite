package xlite.coder;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class XEnum extends AbsCoder {
    @Getter protected final String name;
    @Getter private final List<XEnumField> fields = new ArrayList<>();

    public XEnum(String name, XCoder parent) {
        super(parent);
        this.name = name;
    }

    public void addField(XField field) {
        XEnumField enumField = (XEnumField) field;
        fields.add(enumField);
    }
}
