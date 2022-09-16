package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XCoder;
import xlite.coder.XEnum;

public class ConfEnum extends XEnum {
    @Getter @Setter private String fromExcel;

    public ConfEnum(String name, XCoder parent) {
        super(name, parent);
    }
}
