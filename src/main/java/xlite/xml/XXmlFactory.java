package xlite.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import xlite.coder.*;
import xlite.type.XType;
import xlite.xml.attr.XAttr;
import xlite.xml.element.XElement;

public interface XXmlFactory {
    XElement createElement(Element src, XElement parent);
    XAttr createAttr(Attr src, XElement parent);
    XField createField(String name, XType type, XCoder parent);
    XEnumField createEnumField(String name, XType type, XCoder parent);
    XClass createClass(String name, XCoder parent);
    XEnumer createEnum(String name, XCoder parent);
}
