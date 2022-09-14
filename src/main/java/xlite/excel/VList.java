package xlite.excel;

import xlite.excel.cell.XCell;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class VList {
    private static final int MAX_SAME_NAME_COLUMN = 100;

    public static void read(String title, XRow row, Consumer<XCell> cb) {
        for (int i = 1; i <= MAX_SAME_NAME_COLUMN; i++) {
            try {
                XCell cell = row.getCell(title, i);
                if (!Objects.isNull(cell)) {
                    cb.accept(cell);
                }
            } catch (NullPointerException e) {
                //skip
            }
        }
    }
    public static <T extends Loader> void read(XRow row, Supplier<T> factory, Consumer<T> cb) {
        for (int i = 1; i <= MAX_SAME_NAME_COLUMN; i++) {
            try {
                T loader = factory.get();
                loader.load(row, i);
                cb.accept(loader);
            } catch (NullPointerException e) {
                //skip
            }
        }
    }
}
