package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XEnum;
import xlite.conf.ConfEnum;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;
import xlite.xml.element.EnumElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfEnumElement extends EnumElement {
    public ConfEnumElement(Element src, XElement parent) {
        super(src, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        if (super.checkAttr(name)) {
            return true;
        }
        return XAttr.ATTR_EXCEL.equals(name);
    }

    @Override
    public ConfEnum build0(XmlContext context) {
        XEnum e = super.build0(context);
        ConfEnum confEnum = (ConfEnum) e;
        XAttr fromAttr = getAttr(XAttr.ATTR_EXCEL);
        if (Objects.isNull(fromAttr)) {
            throw new NullPointerException("enum must have a excel attr at enum " + e.getName());
        }
        confEnum.setFromExcel(fromAttr.getValue());
        return confEnum;
    }
}
