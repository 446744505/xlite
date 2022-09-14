package xlite.conf;

import lombok.Getter;
import xlite.coder.XField;

public class ConfField extends XField {
    private final XField field;
    @Getter private final String fromCol;

    public ConfField(XField field, String fromCol) {
        super(field.getName(), field.getType(), field.getParent());
        this.field = field;
        this.fromCol = fromCol;
    }
}
