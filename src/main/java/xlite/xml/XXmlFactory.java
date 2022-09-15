package xlite.xml;

import org.dom4j.Attribute;
import org.dom4j.Element;
import xlite.coder.XClass;
import xlite.coder.XCoder;
import xlite.coder.XField;
import xlite.type.XType;
import xlite.xml.attr.XAttr;
import xlite.xml.element.XElement;

public interface XXmlFactory {
    XElement createElement(Element src, XElement parent);
    XAttr createAttr(Attribute src, XElement parent);
    XField createField(String name, XType type, XCoder parent);
    XClass createClass(String name, XCoder parent);
}
