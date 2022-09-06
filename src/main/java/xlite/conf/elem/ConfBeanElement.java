package xlite.conf.elem;

import org.dom4j.Element;
import xlite.xml.attr.XAttr;
import xlite.xml.element.BeanElement;
import xlite.xml.element.XElement;

public class ConfBeanElement extends BeanElement {
    public ConfBeanElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name)
                || XAttr.ATTR_PARENT.equals(name);
    }

}
