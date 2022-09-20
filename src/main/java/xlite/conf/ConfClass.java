package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XClass;
import xlite.coder.XCoder;
import xlite.coder.XField;
import xlite.coder.XInterface;

import java.util.Objects;

public class ConfClass extends XClass {
    @Getter @Setter private String fromExcel;

    public ConfClass(String name, XCoder parent) {
        super(name, parent);
    }

    public ConfBeanField getIdField() {
        ConfBeanField id = null;
        for (XInterface parent : extendes) {
            if (parent instanceof ConfClass) {
                id = ((ConfClass) parent).getIdField();
                if (Objects.nonNull(id)) {
                    return id;
                }
            }
        }

        for (XField f : fields) {
            ConfBeanField cf = (ConfBeanField) f;
            if (ConfExcelHook.COL_ID_TITLE.equals(cf.getFromCol())) {
                id = (ConfBeanField) f;
                break;
            }
        }
        return id;
    }
}
