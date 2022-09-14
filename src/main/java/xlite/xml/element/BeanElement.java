package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XClass;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.xml.BuildContext;
import xlite.xml.attr.XAttr;

import java.util.Objects;

public class BeanElement extends AbsElement {
    protected XClass buildClass;

    public BeanElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name)
                || XAttr.ATTR_PARENT.equals(name);
    }

    @Override
    public XClass build(BuildContext context) {
        if (!Objects.isNull(buildClass)) {
            return buildClass;
        }

        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        XAttr parentAttr = getAttr(XAttr.ATTR_PARENT);
        buildClass = new XClass(nameAttr.getValue(), parent.build(context));
        if (!Objects.isNull(parentAttr)) {
            String parentName = parentAttr.getValue();
            buildClass.addExtend(XClass.getClass(parentName, parent -> buildClass.addExtend(parent)));
        }
        elements.stream()
                .filter(ele -> ele instanceof VarElement)
                .map(ele -> (VarElement) ele)
                .forEach(ele -> buildClass.addField(ele.build(context)));
        TypeBuilder.registerBean(new XBean(buildClass.getName()));
        afterBuild(context);
        return buildClass;
    }

    protected void afterBuild(BuildContext context) {

    }
}
