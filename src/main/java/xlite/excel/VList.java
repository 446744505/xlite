package xlite.excel;

import xlite.excel.cell.XCell;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class VList {
    private static final int MAX_SAME_NAME_COLUMN = 100;

    public static void read(String title, XRow row, Consumer<XCell> cb) {
        for (int i = 1; i <= MAX_SAME_NAME_COLUMN; i++) {
            try {
                XCell cell = row.getCell(title, i);
                if (Objects.nonNull(cell)) {
                    cb.accept(cell);
                }
            } catch (NullPointerException e) {
                //skip
            }
        }
    }

    public static <T> void read(String title, XRow row, Function<String, T> enumValue, Consumer<T> cb) {
        for (int i = 1; i <= MAX_SAME_NAME_COLUMN; i++) {
            try {
                XCell cell = row.getCell(title, i);
                if (Objects.nonNull(cell)) {
                    String enumKey = cell.asString();
                    T v = enumValue.apply(enumKey);
                    cb.accept(v);
                }
            } catch (NullPointerException e) {
                //skip
            }
        }
    }

    public static <T extends Reader> void read(XRow row, Supplier<T> factory, Consumer<T> cb) {
        for (int i = 1; i <= MAX_SAME_NAME_COLUMN; i++) {
            try {
                T reader = factory.get();
                reader.read(row, i);
                cb.accept(reader);
            } catch (NullPointerException e) {
                //skip
            }
        }
    }
}
