package xlite.coder;

import lombok.Getter;
import lombok.Setter;
import xlite.type.XType;

public class XField extends AbsCoder {
    @Getter private final String name;
    @Getter @Setter private XType type;

    public XField(String name, XType type, XCoder parent) {
        super(parent);
        this.name = name;
        this.type = type;
    }
}
