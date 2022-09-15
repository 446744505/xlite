package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XCoder;
import xlite.coder.XField;
import xlite.type.XType;

public class ConfField extends XField {
    @Getter @Setter private String fromCol;
    @Getter @Setter private String endPoint;

    public ConfField(String name, XType type, XCoder parent) {
        super(name, type, parent);
    }
}
