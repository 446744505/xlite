package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XCoder;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

public class EnumElement extends AbsElement {
    public EnumElement(Element src, XElement parent) {
        super(src, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name);
    }

    @Override
    public XCoder build(XmlContext context) {
        return null;
    }
}
