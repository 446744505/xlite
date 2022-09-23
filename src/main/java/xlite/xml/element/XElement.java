package xlite.xml.element;

import org.w3c.dom.Attr;
import xlite.coder.XCoder;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.List;

public interface XElement {
    String ELEMENT_XLITE = "xlite";
    String ELEMENT_PACKAGE = "package";
    String ELEMENT_BEAN = "bean";
    String ELEMENT_ENUM = "enum";
    String ELEMENT_VAR = "var";

    XElement parse(XElement preEle, XmlContext context);
    XAttr parseAttr(Attr src, XmlContext context);
    <T extends XCoder> T build(XmlContext context);
    String getComment();
    void setComment(String comment);
    List<XElement> getChildren();
}
