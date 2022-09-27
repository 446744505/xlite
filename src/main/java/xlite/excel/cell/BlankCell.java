package xlite.excel.cell;

import xlite.type.inner.Range;

public class BlankCell implements XCell {
    public static final BlankCell INSTANCE = new BlankCell();

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public byte asByte() {
        return 0;
    }

    @Override
    public short asShort() {
        return 0;
    }

    @Override
    public int asInteger() {
        return 0;
    }

    @Override
    public long asLong() {
        return 0;
    }

    @Override
    public float asFloat() {
        return 0;
    }

    @Override
    public double asDouble() {
        return 0;
    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public Range<?> asRange(String valType) {
        return Range.toRange(valType, "");
    }
}
