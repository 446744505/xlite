package xlite.excel.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.Objects;

public interface XCell {
    boolean asBoolean();
    byte asByte();
    short asShort();
    int asInteger();
    long asLong();
    float asFloat();
    double asDouble();
    String asString();

    static XCell createCell(Cell cell) {
        if (Objects.isNull(cell)) {
            return BlankCell.INSTANCE;
        }
        CellType type = cell.getCellType();
        switch (type) {
            case STRING:
                return new StringCell(cell);
            case BOOLEAN:
                return new BoolCell(cell);
            case NUMERIC:
                return new NumberCell(cell);
            case BLANK:
                return BlankCell.INSTANCE;
            default:
                throw new UnsupportedOperationException("unsupported cell type " + type.name());
        }
    }
}
