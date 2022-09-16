package xlite.conf.elem;

import org.dom4j.Element;
import xlite.xml.attr.XAttr;
import xlite.xml.element.EnumVarElement;
import xlite.xml.element.XElement;

public class ConfEnumVarElement extends EnumVarElement {
    public ConfEnumVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        if (super.checkAttr(name)) {
            return true;
        }
        return XAttr.ATTR_COLFROM.equals(name) ||
                XAttr.ATTR_COMMENT.equals(name);
    }
}
