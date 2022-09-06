package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XClass;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.xml.attr.XAttr;

import java.util.Objects;

public class BeanElement extends AbsElement {
    private XClass buildClass;

    public BeanElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name)
                || XAttr.ATTR_PARENT.equals(name);
    }

    @Override
    public XClass build() {
        if (!Objects.isNull(buildClass)) {
            return buildClass;
        }

        XAttr nameAttr = attrs.get(XAttr.ATTR_NAME);
        XAttr parentAttr = attrs.get(XAttr.ATTR_PARENT);
        buildClass = new XClass(nameAttr.getValue(), parent.build());
        if (!Objects.isNull(parentAttr)) {
            String parentName = parentAttr.getValue();
            buildClass.addExtend(XClass.getClass(parentName, parent -> buildClass.addExtend(parent)));
        }
        elements.stream()
                .filter(ele -> ele instanceof VarElement)
                .map(ele -> (VarElement) ele)
                .forEach(ele -> buildClass.addField(ele.build()));
        TypeBuilder.registerBean(new XBean(buildClass.getName()));
        return buildClass;
    }
}
