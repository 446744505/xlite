package xlite.excel;

import lombok.Getter;
import xlite.excel.cell.BlankCell;
import xlite.excel.cell.XCell;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class XRow {
    private final int rowNum;
    @Getter private final XSheet sheet;
    private final Map<Integer, XCell> cells = new HashMap<>();

    public XRow(XSheet sheet, int rowNum) {
        this.sheet = sheet;
        this.rowNum = rowNum;
    }

    public void addCell(int colIndex, XCell cell) {
        if (Objects.isNull(cell)) {
            return;
        }
        cells.put(colIndex, cell);
    }

    public XCell getCell(String title) {
        return getCell(title, 0);
    }

    public XCell getCell(String title, int count) {
        if (count != 0) {
            title += count;
        }
        Integer colIndex = sheet.getHeader().getColIndex(title);
        if (Objects.isNull(colIndex)) {
            throw new NullPointerException(String.format("there is no column %s @ %s", title, sheet));
        }
        XCell cell = cells.get(colIndex);
        if (Objects.isNull(cell)) {
            cell = BlankCell.INSTANCE;
        }
        return cell;
    }

    public <T> T readID() {
        XExcelHook hook = sheet.getExcel().getHook();
        return (T) hook.key(this);
    }

    @Override
    public String toString() {
        return sheet + " at row " + rowNum;
    }

    public static class DefStartRow extends XRow {
        @Getter private final String name;

        public DefStartRow(String name) {
            super(null, 0);
            this.name = name;
        }
    }

    public static class DefEndRow extends XRow {
        public static final DefEndRow INSTANCE = new DefEndRow();
        private DefEndRow() {
            super(null, 0);
        }
    }
}
