package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XClass;
import xlite.coder.XCoder;
import xlite.coder.XField;
import xlite.coder.XInterface;
import xlite.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfClass extends XClass {
    @Getter @Setter private String fromExcel;
    @Getter @Setter private int split;

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

    public List<ConfBeanField> getIndexFields() {
        List<ConfBeanField> rst = new ArrayList<>();

        for (XInterface parent : extendes) {
            if (parent instanceof ConfClass) {
                rst.addAll(((ConfClass) parent).getIndexFields());
            }
        }
        for (XField field : fields) {
            if (!(field instanceof ConfBeanField))
                continue;

            ConfBeanField f = (ConfBeanField) field;
            String index = f.getIndex();
            if (Util.notEmpty(index) && "true".equals(index.toLowerCase())) {
                rst.add(f);
            }
        }
        return rst;
    }
}
