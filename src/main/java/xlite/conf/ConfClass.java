package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XClass;
import xlite.coder.XCoder;

public class ConfClass extends XClass {
    @Getter @Setter private String fromExcel;

    public ConfClass(String name, XCoder parent) {
        super(name, parent);
    }
}
