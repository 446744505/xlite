package xlite;

import xlite.type.TypeBuilder;
import xlite.type.inner.DateTime;
import xlite.type.inner.Range;

import java.util.List;

public interface Checker {
    void check() throws CheckException;

    default void checkByte(byte val, String format) throws CheckException {
        Range<Byte> range = Range.toRange(TypeBuilder.TYPE_BYTE, format);
        if (!range.check(val)) {
            throw new CheckException(String.format("%s is not at range %s", val, format));
        }
    }

    default void checkShort(short val, String format) throws CheckException {
        Range<Short> range = Range.toRange(TypeBuilder.TYPE_SHORT, format);
        if (!range.check(val)) {
            throw new CheckException(String.format("%s is not at range %s", val, format));
        }
    }

    default void checkInteger(int val, String format) throws CheckException {
        Range<Integer> range = Range.toRange(TypeBuilder.TYPE_INT, format);
        if (!range.check(val)) {
            throw new CheckException(String.format("%s is not at range %s", val, format));
        }
    }

    default void checkLong(long val, String format) throws CheckException {
        Range<Long> range = Range.toRange(TypeBuilder.TYPE_LONG, format);
        if (!range.check(val)) {
            throw new CheckException(String.format("%s is not at range %s", val, format));
        }
    }

    default void checkFloat(float val, String format) throws CheckException {
        Range<Float> range = Range.toRange(TypeBuilder.TYPE_FLOAT, format);
        if (!range.check(val)) {
            throw new CheckException(String.format("%s is not at range %s", val, format));
        }
    }

    default void checkDouble(double val, String format) throws CheckException {
        Range<Double> range = Range.toRange(TypeBuilder.TYPE_DOUBLE, format);
        if (!range.check(val)) {
            throw new CheckException(String.format("%s is not at range %s", val, format));
        }
    }

    default void checkString(String val, String format) throws CheckException {
        Range<Integer> range = Range.toRange(TypeBuilder.TYPE_INT, format);
        if (!range.check(val.length())) {
            throw new CheckException(String.format("string %s `length() is not at range %s", val, format));
        }
    }

    default void checkDate(DateTime val, String format) throws CheckException {
        Range<DateTime> range = Range.toRange(TypeBuilder.TYPE_DATE, format);
        if (!range.check(val)) {
            throw new CheckException(String.format("%s is not at range %s", val, format));
        }
    }

    default boolean checkList(List<?> list, String format) {
        return false;
    }
}
