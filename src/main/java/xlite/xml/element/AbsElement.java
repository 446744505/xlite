package xlite.xml.element;

import org.dom4j.*;
import xlite.coder.XCoder;
import xlite.util.Util;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.*;

public abstract class AbsElement implements XElement {
    protected final Element src;
    protected final XElement parent;
    protected String comment;
    protected XCoder buildCache;
    protected final List<XElement> elements = new ArrayList<>();
    private final Map<String, XAttr> attrs = new LinkedHashMap<>();

    public AbsElement(Element src, XElement parent) {
        this.src = src;
        this.parent = parent;
    }

    protected abstract boolean checkAttr(String name);
    protected abstract XCoder build0(XmlContext context);

    protected <T extends XCoder> T setCache(T coder) {
        buildCache = coder;
        return coder;
    }

    @Override
    public final <T extends XCoder> T build(XmlContext context) {
        if (Objects.nonNull(buildCache)) {
            return (T) buildCache;
        }
        build0(context);//这里不赋值，必须在build0里设置cache值
        return (T) buildCache;
    }

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
                if (Objects.nonNull(preEle)) {
                    preEle.setComment(comment.getText());
                }
            } else if (node instanceof Text) {
                Text txt = (Text) node;
                String line = txt.getText();
                if (Util.notEmpty(line)) {
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
        if (Objects.nonNull(this.comment)) {
            this.comment += comment;
        } else {
            this.comment = comment;
        }
    }

    protected <T> T getAttr(String name) {
        return (T) attrs.get(name);
    }
}
