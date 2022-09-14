package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XCoder;
import xlite.xml.BuildContext;
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
    public XCoder build(BuildContext context) {
        return null;
    }
}
