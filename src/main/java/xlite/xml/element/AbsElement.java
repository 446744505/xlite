package xlite.xml.element;

import org.dom4j.Attribute;
import org.dom4j.Element;
import xlite.xml.XXmlFactory;
import xlite.xml.attr.XAttr;

import java.util.*;

public abstract class AbsElement implements XElement {
    protected final Element src;
    protected final XElement parent;
    protected final List<XElement> elements = new ArrayList<>();
    private final Map<String, XAttr> attrs = new LinkedHashMap<>();

    public AbsElement(Element src, XElement parent) {
        this.src = src;
        this.parent = parent;
    }

    protected abstract boolean checkAttr(String name);

    @Override
    public XElement parse(XXmlFactory factory) {
        for (Iterator<Element> it = src.elementIterator(); it.hasNext();) {
            Element ele = it.next();
            XElement child = factory.createElement(ele, this);
            elements.add(child.parse(factory));
        }
        for (Iterator<Attribute> it = src.attributeIterator(); it.hasNext();) {
            Attribute attr = it.next();
            XAttr xAttr = parseAttr(attr, factory);
            attrs.put(xAttr.getName(), xAttr);
        }
        return this;
    }

    @Override
    public XAttr parseAttr(Attribute src, XXmlFactory factory) {
        String attrName = src.getName();
        if (!checkAttr(attrName)) {
            throw new UnsupportedOperationException(String.format("element [%s] unsupported attribute [%s]", this.src.getName(), attrName));
        }
        return factory.createAttr(src, this);
    }

    protected <T> T getAttr(String name) {
        return (T) attrs.get(name);
    }
}
