package xlite.coder;

import lombok.Getter;
import lombok.Setter;
import xlite.type.XType;

public class XField extends AbsCoder {
    @Getter private final String name;
    @Getter private boolean staticed;
    @Getter @Setter private XType type;
    @Getter @Setter private String comment;
    @Getter @Setter private String rangeCheck = "";

    public XField(String name, XType type, XCoder parent) {
        super(parent);
        this.name = name;
        this.type = type;
    }

    public XField staticed() {
        staticed = true;
        return this;
    }
}
