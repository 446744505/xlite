package xlite.excel.cell;

import org.apache.poi.ss.usermodel.Cell;

public class BoolCell extends AbsCell {
    protected BoolCell(Cell cell) {
        super(cell);
    }

    @Override
    public boolean asBoolean() {
        return cell.getBooleanCellValue();
    }

    @Override
    public byte asByte() {
        return (byte) (asBoolean() ? 1 : 0);
    }

    @Override
    public short asShort() {
        return (short) (asBoolean() ? 1 : 0);
    }

    @Override
    public int asInteger() {
        return asBoolean() ? 1 : 0;
    }

    @Override
    public long asLong() {
        return asBoolean() ? 1 : 0;
    }

    @Override
    public float asFloat() {
        return asBoolean() ? 1 : 0;
    }

    @Override
    public double asDouble() {
        return asBoolean() ? 1 : 0;
    }

    @Override
    public String asString() {
        return asBoolean() ? "true" : "false";
    }
}
