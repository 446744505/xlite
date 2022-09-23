package xlite.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import xlite.coder.*;
import xlite.type.XType;
import xlite.xml.attr.SimpleAttr;
import xlite.xml.attr.TypeAttr;
import xlite.xml.attr.XAttr;
import xlite.xml.element.*;

public class SimpleXmlFactory implements XXmlFactory {

    @Override
    public XElement createElement(Element src, XElement parent) {
        String name = src.getNodeName();
        if (XElement.ELEMENT_PACKAGE.equals(name)) {
            return new PackageElement(src, parent);
        } else if (XElement.ELEMENT_BEAN.equals(name)) {
            return new BeanElement(src, parent);
        } else if (XElement.ELEMENT_VAR.equals(name)) {
            if (parent instanceof EnumElement) {
                return new EnumVarElement(src, parent);
            }
            return new BeanVarElement(src, parent);
        } else {
            return new NullElement();
        }
    }

    @Override
    public XAttr createAttr(Attr src, XElement parent) {
        String name = src.getName();
        if (XAttr.ATTR_TYPE.equals(name)) {
            return new TypeAttr(src, parent);
        } else {
            return new SimpleAttr(src, parent);
        }
    }

    @Override
    public XField createField(String name, XType type, XCoder parent) {
        return new XField(name, type, parent);
    }

    @Override
    public XEnumField createEnumField(String name, XType type, XCoder parent) {
        return new XEnumField(name, type, parent);
    }

    @Override
    public XClass createClass(String name, XCoder parent) {
        return new XClass(name, parent);
    }

    @Override
    public XEnumer createEnum(String name, XCoder parent) {
        return new XEnumer(name, parent);
    }
}
