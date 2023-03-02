package xlite.conf;

import xlite.coder.XCoder;
import xlite.coder.XEnumField;
import xlite.excel.cell.XCell;
import xlite.language.Java;
import xlite.type.XAny;
import xlite.type.XType;

public class ConfEnumField extends XEnumField {
    private String fromCol;
    private String excel;

    public ConfEnumField(String name, XType type, XCoder parent) {
        super(name, type, parent);
    }

    public void setCellValue(XCell cell) {
        XType type = getType();
        if (type instanceof XAny) {
            setType(new XAny());
        }
        String val = getType().accept(new TypeOfCellValue(cell), Java.INSTANCE);
        setValue(val);
    }

    public String getFromCol() {
        return fromCol;
    }

    public void setFromCol(String fromCol) {
        this.fromCol = fromCol;
    }

    public String getExcel() {
        return excel;
    }

    public void setExcel(String excel) {
        this.excel = excel;
    }
}
