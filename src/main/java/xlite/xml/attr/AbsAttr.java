package xlite.xml.attr;

import org.w3c.dom.Attr;
import xlite.xml.element.XElement;

public abstract class AbsAttr implements XAttr {
    protected final Attr src;
    protected final XElement parent;

    public AbsAttr(Attr src, XElement parent) {
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
