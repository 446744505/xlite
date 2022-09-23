package xlite.conf.elem;

import org.w3c.dom.Element;
import xlite.coder.XField;
import xlite.conf.ConfEnumField;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;
import xlite.xml.element.EnumVarElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfEnumVarElement extends EnumVarElement {
    public ConfEnumVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public ConfEnumField build0(XmlContext context) {
        XField field = super.build0(context);
        ConfEnumField confEnumField = (ConfEnumField) field;
        XAttr commentAttr = getAttr(XAttr.ATTR_COMMENT);
        if (Objects.nonNull(commentAttr)) {
            field.setComment(commentAttr.getValue());
        }
        XAttr valueAttr = getAttr(XAttr.ATTR_VALUE);
        XAttr fromAttr = getAttr(XAttr.ATTR_COLFROM);
        if (Objects.isNull(valueAttr) && Objects.isNull(fromAttr)) {
            throw new NullPointerException("var mush have a value or from attr at conf enum " + getParentName());
        }
        if (Objects.nonNull(fromAttr)) {
            confEnumField.setFromCol(fromAttr.getValue());
        }
        XAttr excelAttr = getAttr(XAttr.ATTR_EXCEL);
        if (Objects.nonNull(excelAttr)) {
            confEnumField.setExcel(excelAttr.getValue());
        }
        return confEnumField;
    }

    @Override
    protected boolean checkValueAttr() {
        return false;
    }
}
