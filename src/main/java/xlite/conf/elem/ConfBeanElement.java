package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XClass;
import xlite.conf.ConfClass;
import xlite.conf.PrintLoadMethod;
import xlite.xml.XmlContext;
import xlite.xml.attr.SimpleAttr;
import xlite.xml.attr.XAttr;
import xlite.xml.element.BeanElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfBeanElement extends BeanElement {
    public ConfBeanElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name)
                || XAttr.ATTR_PARENT.equals(name)
                || XAttr.ATTR_EXCEL.equals(name);
    }

    @Override
    public XClass build(XmlContext context) {
        XClass clazz = super.build(context);
        ConfClass confClass = (ConfClass) clazz;
        SimpleAttr excelAttr = getAttr(XAttr.ATTR_EXCEL);
        if (!Objects.isNull(excelAttr)) {
            confClass.setFromExcel(excelAttr.getValue());
        }
        return confClass;
    }

    @Override
    public void afterBuild(XmlContext context) {
        if (context.isConfLoadCode()) {
            new PrintLoadMethod(buildClass, null).make();
        }
    }
}
