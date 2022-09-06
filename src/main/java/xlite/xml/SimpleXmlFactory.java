package xlite.xml;

import org.dom4j.Attribute;
import org.dom4j.Element;
import xlite.xml.attr.*;
import xlite.xml.element.*;

public class SimpleXmlFactory implements XXmlFactory {

    @Override
    public XElement createElement(Element src, XElement parent) {
        String name = src.getName();
        if (XElement.ELEMENT_PACKAGE.equals(name)) {
            return new PackageElement(src, parent);
        } else if (XElement.ELEMENT_BEAN.equals(name)) {
            return new BeanElement(src, parent);
        } else if (XElement.ELEMENT_ENUM.equals(name)) {
            return new EnumElement(src, parent);
        } else if (XElement.ELEMENT_VAR.equals(name)) {
            return new VarElement(src, parent);
        }
        throw new UnsupportedOperationException(String.format("unsupported element %s", name));
    }

    @Override
    public XAttr createAttr(Attribute src, XElement parent) {
        String name = src.getName();
        if (XAttr.ATTR_NAME.equals(name)) {
            return new NameAttr(src, parent);
        } else if (XAttr.ATTR_TYPE.equals(name)) {
            return new TypeAttr(src, parent);
        } else if (XAttr.ATTR_KEY.equals(name)) {
            return new KeyAttr(src, parent);
        } else if (XAttr.ATTR_VALUE.equals(name)) {
            return new ValueAttr(src, parent);
        } else if (XAttr.ATTR_COLFROM.equals(name)) {
            return new ColFromAttr(src, parent);
        } else if (XAttr.ATTR_PARENT.equals(name)) {
            return new ParentAttr(src, parent);
        }
        throw new UnsupportedOperationException(String.format("unsupported attribute %s", name));
    }
}
