package xlite.xml.element;

import org.w3c.dom.Element;
import xlite.coder.XCoder;
import xlite.coder.XEnumField;
import xlite.coder.XField;
import xlite.type.XType;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.Objects;

public class EnumVarElement extends VarElement {
    public EnumVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public String getParentName() {
        EnumElement p = (EnumElement) parent;
        return p.getName();
    }

    @Override
    protected XField createField(XmlContext context, String name, XType type, XCoder parent) {
        return context.getFactory().createEnumField(name, type, parent);
    }

    @Override
    public XEnumField build0(XmlContext context) {
        XField field = super.build0(context);
        XEnumField enumField = (XEnumField) field;
        XAttr valueAttr = getAttr(XAttr.ATTR_VALUE);
        if (checkValueAttr() && Objects.isNull(valueAttr)) {
            throw new NullPointerException("var mush have a value attr at enum " + getParentName());
        }
        XAttr commentAttr = getAttr(XAttr.ATTR_COMMENT);
        if (Objects.nonNull(commentAttr)) {
            field.setComment(commentAttr.getValue());
        }
        if (Objects.nonNull(valueAttr)) {
            enumField.setValue(valueAttr.getValue());
        }
        return enumField;
    }

    protected boolean checkValueAttr() {
        return true;
    }
}
