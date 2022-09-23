package xlite.xml.element;

import org.w3c.dom.Attr;
import xlite.coder.XCoder;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.List;

public class NullElement implements XElement {
    @Override
    public XElement parse(XElement preEle, XmlContext context) {
        return null;
    }

    @Override
    public XAttr parseAttr(Attr src, XmlContext context) {
        return null;
    }

    @Override
    public <T extends XCoder> T build(XmlContext context) {
        return null;
    }

    @Override
    public String getComment() {
        return null;
    }

    @Override
    public void setComment(String comment) {

    }

    @Override
    public List<XElement> getChildren() {
        return null;
    }
}
