package xlite.excel;

import lombok.Getter;
import xlite.excel.cell.XCell;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class XRow {
    @Getter private final XSheet sheet;
    private final Map<Integer, XCell> cells = new HashMap<>();

    public XRow(XSheet sheet) {
        this.sheet = sheet;
    }

    public void addCell(int colIndex, XCell cell) {
        if (Objects.isNull(cell)) {
            return;
        }
        cells.put(colIndex, cell);
    }

    public XCell getCell(String title) {
        Integer colIndex = sheet.getHeader().getColIndex(title);
        if (Objects.isNull(colIndex)) {
            throw new NullPointerException(String.format("there is no title %s @ %s", title, sheet));
        }
        return cells.get(colIndex);
    }

    public static class DefStartRow extends XRow {
        @Getter private final String name;

        public DefStartRow(String name) {
            super(null);
            this.name = name;
        }
    }

    public static class DefEndRow extends XRow {
        public static final DefEndRow INSTANCE = new DefEndRow();
        private DefEndRow() {
            super(null);
        }
    }
}
