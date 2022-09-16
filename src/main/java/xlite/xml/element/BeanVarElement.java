package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XCoder;
import xlite.coder.XField;
import xlite.type.XType;
import xlite.xml.XmlContext;

public class BeanVarElement extends VarElement {
    public BeanVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public String getParentName() {
        BeanElement p = (BeanElement) parent;
        return p.getName();
    }

    @Override
    protected XField createField(XmlContext context, String name, XType type, XCoder parent) {
        return context.getFactory().createField(name, type, parent);
    }
}
