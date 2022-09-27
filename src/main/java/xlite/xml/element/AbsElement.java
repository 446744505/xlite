package xlite.xml.element;

import org.w3c.dom.*;
import xlite.coder.XCoder;
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
        NodeList nodes = src.getChildNodes();
        for(int i = 0, size = nodes.getLength(); i < size; i++) {
            Node node = nodes.item(i);
            if(!(node instanceof Element)) {
                continue;
            }
            Element ele = (Element) node;
            preEle = context.getFactory().createElement(ele, this);
            elements.add(preEle.parse(preEle, context));
        }
        NamedNodeMap attrs = src.getAttributes();
        for (int i = 0, size = attrs.getLength(); i < size; i++) {
            Attr attr = (Attr) attrs.item(i);
            XAttr xAttr = parseAttr(attr, context);
            this.attrs.put(xAttr.getName(), xAttr);
        }

        Node c = src.getNextSibling();
        if (c != null && Node.TEXT_NODE == c.getNodeType()) {
            comment = c.getTextContent().trim().replaceAll("[\r\n]", "");
        }
        setComment(comment);

        return this;
    }

    @Override
    public XAttr parseAttr(Attr src, XmlContext context) {
        return context.getFactory().createAttr(src, this);
    }

    @Override
    public List<XElement> getChildren() {
        return elements;
    }

    @Override
    public String getComment() {
        return Objects.isNull(comment) ? "" : comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    protected <T> T getAttr(String name) {
        return (T) attrs.get(name);
    }
}
