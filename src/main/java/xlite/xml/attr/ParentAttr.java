package xlite.xml.attr;

import org.dom4j.Attribute;
import xlite.xml.element.XElement;

public class ParentAttr extends AbsAttr {
    public ParentAttr(Attribute src, XElement parent) {
        super(src, parent);
    }
}
