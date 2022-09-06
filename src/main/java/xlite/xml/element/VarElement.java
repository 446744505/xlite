package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XField;
import xlite.xml.attr.KeyAttr;
import xlite.xml.attr.TypeAttr;
import xlite.xml.attr.ValueAttr;
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
    public XField build() {
        if (!Objects.isNull(buildField)) {
            return buildField;
        }

        XAttr nameAttr = attrs.get(XAttr.ATTR_NAME);
        TypeAttr typeAttr = (TypeAttr) attrs.get(XAttr.ATTR_TYPE);
        KeyAttr keyAttr = (KeyAttr) attrs.get(XAttr.ATTR_KEY);
        ValueAttr valueAttr = (ValueAttr) attrs.get(XAttr.ATTR_VALUE);
        buildField = new XField(nameAttr.getValue(), typeAttr.build(keyAttr, valueAttr, t -> buildField.setType(t)), parent.build());
        return buildField;
    }
}
