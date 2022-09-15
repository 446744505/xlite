package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XCoder;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;
import xlite.xml.element.AbsElement;
import xlite.xml.element.XElement;

public class ConfEnumElement extends AbsElement {
    public ConfEnumElement(Element src, XElement parent) {
        super(src, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name) ||
                XAttr.ATTR_EXCEL.equals(name);
    }

    @Override
    public XCoder build(XmlContext context) {
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        XAttr typeAttr = getAttr(XAttr.ATTR_TYPE);
        XAttr fromAttr = getAttr(XAttr.ATTR_COLFROM);
        XAttr commentAttr = getAttr(XAttr.ATTR_COMMENT);
        return null;
    }
}
