package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XField;
import xlite.xml.XmlContext;
import xlite.xml.attr.SimpleAttr;
import xlite.xml.attr.TypeAttr;
import xlite.xml.attr.XAttr;

import java.util.Objects;

public class VarElement extends AbsElement {
    protected XField buildField;

    public VarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name) ||
                XAttr.ATTR_TYPE.equals(name);
    }

    @Override
    public XField build(XmlContext context) {
        if (!Objects.isNull(buildField)) {
            return buildField;
        }

        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        TypeAttr typeAttr = getAttr(XAttr.ATTR_TYPE);
        SimpleAttr keyAttr = getAttr(XAttr.ATTR_KEY);
        SimpleAttr valueAttr = getAttr(XAttr.ATTR_VALUE);
        buildField = context.getFactory().createField(nameAttr.getValue(), typeAttr.build(keyAttr, valueAttr, t -> buildField.setType(t)), parent.build(context));
        buildField.setComment(getComment());
        return buildField;
    }
}
