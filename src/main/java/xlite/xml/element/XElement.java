package xlite.xml.element;

import org.dom4j.Attribute;
import xlite.coder.XCoder;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

public interface XElement {
    String ELEMENT_PACKAGE = "package";
    String ELEMENT_BEAN = "bean";
    String ELEMENT_ENUM = "enum";
    String ELEMENT_VAR = "var";

    XElement parse(XElement preEle, XmlContext context);
    XAttr parseAttr(Attribute src, XmlContext context);
    <T extends XCoder> T build(XmlContext context);
    String getComment();
    void setComment(String comment);
}
