package xlite.excel.cell;

import org.apache.poi.ss.usermodel.Cell;

public abstract class AbsCell<T> implements XCell {
    protected final Cell cell;

    protected AbsCell(Cell cell) {
        this.cell = cell;
    }
}
