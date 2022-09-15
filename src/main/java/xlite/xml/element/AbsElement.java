package xlite.xml.element;

import org.dom4j.*;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.*;

public abstract class AbsElement implements XElement {
    protected final Element src;
    protected final XElement parent;
    protected String comment;
    protected final List<XElement> elements = new ArrayList<>();
    private final Map<String, XAttr> attrs = new LinkedHashMap<>();

    public AbsElement(Element src, XElement parent) {
        this.src = src;
        this.parent = parent;
    }

    protected abstract boolean checkAttr(String name);

    @Override
    public XElement parse(XElement preEle, XmlContext context) {
        for (int i = 0; i < src.nodeCount(); i++) {
            Node node = src.node(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                preEle = context.getFactory().createElement(ele, this);
                elements.add(preEle.parse(preEle, context));
            } else if (node instanceof Comment) {
                Comment comment = (Comment) node;
                if (!Objects.isNull(preEle)) {
                    preEle.setComment(comment.getText());
                }
            } else if (node instanceof Text) {
                Text txt = (Text) node;
                String line = txt.getText();
                if (!Objects.isNull(preEle) && !line.isEmpty()) {
                    preEle.setComment(line.trim());
                }
            }
        }
        for (Iterator<Attribute> it = src.attributeIterator(); it.hasNext();) {
            Attribute attr = it.next();
            XAttr xAttr = parseAttr(attr, context);
            attrs.put(xAttr.getName(), xAttr);
        }
        return this;
    }

    @Override
    public XAttr parseAttr(Attribute src, XmlContext context) {
        String attrName = src.getName();
        if (!checkAttr(attrName)) {
            throw new UnsupportedOperationException(String.format("element [%s] unsupported attribute [%s]", this.src.getName(), attrName));
        }
        return context.getFactory().createAttr(src, this);
    }

    @Override
    public String getComment() {
        return Objects.isNull(comment) ? "" : comment;
    }

    @Override
    public void setComment(String comment) {
        if (!Objects.isNull(this.comment)) {
            this.comment += comment;
        } else {
            this.comment = comment;
        }
    }

    protected <T> T getAttr(String name) {
        return (T) attrs.get(name);
    }
}
