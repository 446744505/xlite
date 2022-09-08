package xlite.excel.cell;

import org.apache.poi.ss.usermodel.Cell;

public class NumberCell extends AbsCell {
    protected NumberCell(Cell cell) {
        super(cell);
    }

    @Override
    public boolean asBoolean() {
        return asDouble() == 0 ? false : true;
    }

    @Override
    public byte asByte() {
        double v = asDouble();
        if (v < Byte.MIN_VALUE || v > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("overflow byte value " + v);
        }
        byte r = (byte) v;
        if (r != v) {
            throw new IllegalArgumentException("illegal byte value " + v);
        }
        return r;
    }

    @Override
    public short asShort() {
        double v = asDouble();
        if (v < Short.MIN_VALUE || v > Short.MAX_VALUE) {
            throw new IllegalArgumentException("overflow short value " + v);
        }
        short r = (short) v;
        if (r != v) {
            throw new IllegalArgumentException("illegal short value " + v);
        }
        return r;
    }

    @Override
    public int asInteger() {
        double v = asDouble();
        if (v < Integer.MIN_VALUE || v > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("overflow int value " + v);
        }
        int r = (int) v;
        if (r != v) {
            throw new IllegalArgumentException("illegal int value " + v);
        }
        return r;
    }

    @Override
    public long asLong() {
        double v = asDouble();
        if (v < Long.MIN_VALUE || v > Long.MAX_VALUE) {
            throw new IllegalArgumentException("overflow long value " + v);
        }
        long r = (long) v;
        if (r != v) {
            throw new IllegalArgumentException("illegal long value " + v);
        }
        return r;
    }

    @Override
    public float asFloat() {
        double v = asDouble();
        if (v < Float.MIN_VALUE || v > Float.MAX_VALUE) {
            throw new IllegalArgumentException("overflow float value " + v);
        }
        float r = (float) v;
        if (r != v) {
            throw new IllegalArgumentException("illegal float value " + v);
        }
        return r;
    }

    @Override
    public double asDouble() {
        return cell.getNumericCellValue();
    }

    @Override
    public String asString() {
        double v = asDouble();
        return String.valueOf(v);
    }
}
