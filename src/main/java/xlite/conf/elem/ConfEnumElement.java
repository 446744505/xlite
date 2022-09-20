package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XEnumer;
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
        XEnumer e = super.build0(context);
        ConfEnum confEnum = (ConfEnum) e;
        XAttr fromAttr = getAttr(XAttr.ATTR_EXCEL);
        if (Objects.nonNull(fromAttr)) {//excel attr可以放在var上
            confEnum.setFromExcel(fromAttr.getValue());
        }
        return confEnum;
    }
}
