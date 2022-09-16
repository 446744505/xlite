package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XField;
import xlite.conf.ConfBeanField;
import xlite.conf.ConfGenerator;
import xlite.xml.XmlContext;
import xlite.xml.attr.SimpleAttr;
import xlite.xml.attr.XAttr;
import xlite.xml.element.BeanVarElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfBeanVarElement extends BeanVarElement {
    public ConfBeanVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        if (super.checkAttr(name)) {
            return true;
        }
        return XAttr.ATTR_COLFROM.equals(name) ||
                XAttr.ATTR_ENDPOINT.equals(name) ||
                XAttr.ATTR_COMMENT.equals(name);
    }

    @Override
    public ConfBeanField build(XmlContext context) {
        if (!Objects.isNull(buildField)) {
            return (ConfBeanField) buildField;
        }

        SimpleAttr endPointAttr = getAttr(XAttr.ATTR_ENDPOINT);
        String endPoint = Objects.isNull(endPointAttr) ? ConfGenerator.ENDPOINT_ALL : endPointAttr.getValue();
        if (!ConfGenerator.ENDPOINT_ALL.equals(endPoint) &&
                !ConfGenerator.ENDPOINT_ALL.equals(context.getEndPoint()) &&
                !context.getEndPoint().equals(endPoint)) {
            return null;
        }

        XField field = super.build(context);
        ConfBeanField confField = (ConfBeanField) field;
        SimpleAttr colFrom = getAttr(XAttr.ATTR_COLFROM);
        confField.setFromCol(Objects.isNull(colFrom) ? "" : colFrom.getValue());
        confField.setEndPoint(endPoint);
        return confField;
    }
}
