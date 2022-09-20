package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XCoder;
import xlite.coder.XEnumField;
import xlite.excel.cell.XCell;
import xlite.language.Java;
import xlite.type.XAny;
import xlite.type.XType;

public class ConfEnumField extends XEnumField {
    @Getter @Setter private String fromCol;
    @Getter @Setter private String excel;

    public ConfEnumField(String name, XType type, XCoder parent) {
        super(name, type, parent);
    }

    public void setCellValue(XCell cell) {
        XType type = getType();
        if (type instanceof XAny) {
            setType(new XAny());
        }
        Object val = getType().accept(new TypeOfCellValue(cell), Java.INSTANCE);
        setValue(val.toString());
    }
}
