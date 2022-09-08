package xlite.excel.cell;

import org.apache.poi.ss.usermodel.Cell;

public class StringCell extends AbsCell {
    protected StringCell(Cell cell) {
        super(cell);
    }

    @Override
    public boolean asBoolean() {
        return Boolean.valueOf(asString());
    }

    @Override
    public byte asByte() {
        return Byte.valueOf(asString());
    }

    @Override
    public short asShort() {
        return Short.valueOf(asString());
    }

    @Override
    public int asInteger() {
        return Integer.valueOf(asString());
    }

    @Override
    public long asLong() {
        return Long.valueOf(asString());
    }

    @Override
    public float asFloat() {
        return Float.valueOf(asString());
    }

    @Override
    public double asDouble() {
        return Double.valueOf(asString());
    }

    @Override
    public String asString() {
        return cell.getStringCellValue();
    }
}
