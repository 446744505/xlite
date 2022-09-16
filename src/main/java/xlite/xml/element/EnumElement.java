package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XEnum;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.Objects;

public class EnumElement extends AbsElement {
    private XEnum buildEnum;

    public EnumElement(Element src, XElement parent) {
        super(src, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name);
    }

    @Override
    public XEnum build(XmlContext context) {
        if (!Objects.isNull(buildEnum)) {
            return buildEnum;
        }
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        if (Objects.isNull(nameAttr)) {
            throw new NullPointerException("enum must have a name attr");
        }
        String name = nameAttr.getValue();
        buildEnum = context.getFactory().createEnum(name, parent.build(context));
        elements.stream()
                .filter(ele -> ele instanceof VarElement)
                .map(ele -> (VarElement) ele)
                .forEach(ele -> buildEnum.addField(ele.build(context)));
        return buildEnum;
    }

    public String getName() {
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        return nameAttr.getValue();
    }
}
