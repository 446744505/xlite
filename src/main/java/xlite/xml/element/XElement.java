package xlite.xml.element;

import org.dom4j.Attribute;
import xlite.coder.XCoder;
import xlite.xml.XXmlFactory;
import xlite.xml.attr.XAttr;

public interface XElement {
    String ELEMENT_PACKAGE = "package";
    String ELEMENT_BEAN = "bean";
    String ELEMENT_ENUM = "enum";
    String ELEMENT_VAR = "var";

    XElement parse(XXmlFactory factory);
    XAttr parseAttr(Attribute src, XXmlFactory factory);
    XCoder build();
}
