package xlite.xml;

import org.dom4j.Attribute;
import org.dom4j.Element;
import xlite.coder.*;
import xlite.type.XType;
import xlite.xml.attr.XAttr;
import xlite.xml.element.XElement;

public interface XXmlFactory {
    XElement createElement(Element src, XElement parent);
    XAttr createAttr(Attribute src, XElement parent);
    XField createField(String name, XType type, XCoder parent);
    XEnumField createEnumField(String name, XType type, XCoder parent);
    XClass createClass(String name, XCoder parent);
    XEnum createEnum(String name, XCoder parent);
}
