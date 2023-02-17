package xlite.conf.elem;

import org.w3c.dom.Element;
import xlite.coder.XClass;
import xlite.conf.ConfClass;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;
import xlite.xml.element.BeanElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfBeanElement extends BeanElement {
    public ConfBeanElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public ConfClass build0(XmlContext context) {
        XClass clazz = super.build0(context);
        ConfClass confClass = (ConfClass) clazz;
        XAttr excelAttr = getAttr(XAttr.ATTR_EXCEL);
        if (Objects.nonNull(excelAttr)) {
            confClass.setFromExcel(excelAttr.getValue());
        }
        XAttr splitAttr = getAttr(XAttr.ATTR_SPLIT);
        if (Objects.nonNull(splitAttr)) {
            confClass.setSplit(Integer.valueOf(splitAttr.getValue()));
        }
        return confClass;
    }
}
