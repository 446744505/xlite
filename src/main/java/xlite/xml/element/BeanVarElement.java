package xlite.xml.element;

import org.dom4j.Element;

public class BeanVarElement extends VarElement {
    public BeanVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public String getParentName() {
        BeanElement p = (BeanElement) parent;
        return p.getName();
    }
}
