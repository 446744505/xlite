package xlite.conf.elem;

import org.dom4j.Element;
import xlite.xml.attr.XAttr;
import xlite.xml.element.VarElement;
import xlite.xml.element.XElement;

public class ConfVarElement extends VarElement {
    public ConfVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name) ||
                XAttr.ATTR_TYPE.equals(name) ||
                XAttr.ATTR_KEY.equals(name) ||
                XAttr.ATTR_VALUE.equals(name) ||
                XAttr.ATTR_COLFROM.equals(name);
    }
}
