package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XCoder;
import xlite.coder.XField;
import xlite.type.XType;
import xlite.xml.XmlContext;
import xlite.xml.attr.TypeAttr;
import xlite.xml.attr.XAttr;

import java.util.Objects;

public abstract class VarElement extends AbsElement {
    public VarElement(Element element, XElement parent) {
        super(element, parent);
    }

    public abstract String getParentName();
    protected abstract XField createField(XmlContext context, String name, XType type, XCoder parent);

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name) ||
                XAttr.ATTR_TYPE.equals(name) ||
                XAttr.ATTR_KEY.equals(name) ||
                XAttr.ATTR_VALUE.equals(name);
    }

    @Override
    public XField build0(XmlContext context) {
        String name = getParentName();
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        if (Objects.isNull(nameAttr)) {
            throw new NullPointerException("var must have a name attr at bean " + name);
        }
        TypeAttr typeAttr = getAttr(XAttr.ATTR_TYPE);
        if (Objects.isNull(typeAttr)) {
            throw new NullPointerException("var must have a type attr at bean " + name);
        }
        XAttr keyAttr = getAttr(XAttr.ATTR_KEY);
        XAttr valueAttr = getAttr(XAttr.ATTR_VALUE);
        XField field = setCache(createField(context, nameAttr.getValue(),
                typeAttr.build(keyAttr, valueAttr, t -> ((XField)buildCache).setType(t)), parent.build(context)));
        field.setComment(getComment());
        return field;
    }
}
