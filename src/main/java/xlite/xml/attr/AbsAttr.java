package xlite.xml.attr;

import org.dom4j.Attribute;
import xlite.xml.element.XElement;

public abstract class AbsAttr implements XAttr {
    protected final Attribute src;
    protected final XElement parent;

    public AbsAttr(Attribute src, XElement parent) {
        this.src = src;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return src.getName();
    }

    @Override
    public String getValue() {
        return src.getValue();
    }
}
