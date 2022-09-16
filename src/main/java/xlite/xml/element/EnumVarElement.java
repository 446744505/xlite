package xlite.xml.element;

import org.dom4j.Element;
import xlite.xml.attr.XAttr;

public class EnumVarElement extends VarElement {
    public EnumVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public String getParentName() {
        EnumElement p = (EnumElement) parent;
        return p.getName();
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name) ||
                XAttr.ATTR_TYPE.equals(name) ||
                XAttr.ATTR_VALUE.equals(name);
    }
}
