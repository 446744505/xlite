package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XField;
import xlite.conf.ConfField;
import xlite.conf.ConfGenerator;
import xlite.xml.XmlContext;
import xlite.xml.attr.SimpleAttr;
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
                XAttr.ATTR_COLFROM.equals(name) ||
                XAttr.ATTR_ENDPOINT.equals(name);
    }

    @Override
    public XField build(XmlContext context) {
        SimpleAttr endPointAttr = getAttr(XAttr.ATTR_ENDPOINT);
        String endPoint = Objects.isNull(endPointAttr) ? ConfGenerator.ENDPOINT_ALL : endPointAttr.getValue();
        if (!ConfGenerator.ENDPOINT_ALL.equals(endPoint) &&
                !ConfGenerator.ENDPOINT_ALL.equals(context.getEndPoint()) &&
                !context.getEndPoint().equals(endPoint)) {
            return null;
        }

        XField field = super.build(context);
        ConfField confField = (ConfField) field;
        SimpleAttr colFrom = getAttr(XAttr.ATTR_COLFROM);

        confField.setFromCol(Objects.isNull(colFrom) ? "" : colFrom.getValue());
        confField.setEndPoint(endPoint);
        return confField;
    }
}
