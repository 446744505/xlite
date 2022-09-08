package xlite.conf;

import xlite.excel.XExcelHook;
import xlite.excel.XRow;
import xlite.excel.XSheet;
import xlite.excel.cell.XCell;

import java.util.Objects;

public class ConfExcelHook implements XExcelHook {
    private static final String COL_ID_TITLE = "ID";
    @Override
    public void checkHeader(int colIndex, String title, XSheet sheet) {
        if (colIndex == 0 && !COL_ID_TITLE.equals(title)) {
            throw new IllegalStateException("conf excel cell(0, 0) must a ID cell @ " + sheet);
        }
    }

    @Override
    public Object key(XRow row) {
        XCell idCell = row.getCell(COL_ID_TITLE);
        XSheet sheet = row.getSheet();
        if (Objects.isNull(idCell)) {
            throw new IllegalStateException(String.format("there is no id column @ %s", sheet));
        }
        return idCell.asInteger();
    }
}
