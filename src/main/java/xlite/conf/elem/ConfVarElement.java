package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XField;
import xlite.conf.ConfField;
import xlite.xml.XmlContext;
import xlite.xml.attr.ColFromAttr;
import xlite.xml.attr.XAttr;
import xlite.xml.element.VarElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfVarElement extends VarElement {
    public ConfVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name) ||
                XAttr.ATTR_TYPE.equals(name) ||
                XAttr.ATTR_KEY.equals(name) ||
                XAttr.ATTR_VALUE.equals(name) ||
                XAttr.ATTR_COLFROM.equals(name);
    }

    @Override
    public XField build(XmlContext context) {
        XField field = super.build(context);
        ConfField confField = (ConfField) field;
        ColFromAttr colFrom = getAttr(XAttr.ATTR_COLFROM);
        confField.setFromCol(Objects.isNull(colFrom) ? "" : colFrom.getValue());
        return confField;
    }
}
