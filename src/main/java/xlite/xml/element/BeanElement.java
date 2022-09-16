package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XClass;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.Objects;

public class BeanElement extends AbsElement {
    public BeanElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name)
                || XAttr.ATTR_PARENT.equals(name);
    }

    @Override
    public XClass build0(XmlContext context) {
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        if (Objects.isNull(nameAttr)) {
            throw new NullPointerException("bean must have a name attr");
        }
        XAttr parentAttr = getAttr(XAttr.ATTR_PARENT);
        XClass clazz = setCache(context.getFactory().createClass(nameAttr.getValue(), parent.build(context)));
        clazz.setComment(getComment());
        if (Objects.nonNull(parentAttr)) {
            String parentName = parentAttr.getValue();
            clazz.addExtend(XClass.getClass(parentName, parent -> clazz.addExtend(parent)));
        }
        elements.stream()
                .filter(ele -> ele instanceof VarElement)
                .map(ele -> (VarElement) ele)
                .forEach(ele -> clazz.addField(ele.build(context)));
        TypeBuilder.registerBean(new XBean(clazz.getName()));
        return clazz;
    }

    public String getName() {
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        return nameAttr.getValue();
    }
}
