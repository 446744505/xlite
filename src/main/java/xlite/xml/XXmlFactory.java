package xlite.xml;

import org.dom4j.Attribute;
import org.dom4j.Element;
import xlite.xml.attr.XAttr;
import xlite.xml.element.XElement;

public interface XXmlFactory {
    XElement createElement(Element src, XElement parent);
    XAttr createAttr(Attribute src, XElement parent);
}
