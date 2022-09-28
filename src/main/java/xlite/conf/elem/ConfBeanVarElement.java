package xlite.conf.elem;

import org.w3c.dom.Element;
import xlite.coder.XField;
import xlite.conf.ConfBeanField;
import xlite.conf.ConfGenerator;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;
import xlite.xml.element.BeanVarElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfBeanVarElement extends BeanVarElement {
    public ConfBeanVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public ConfBeanField build0(XmlContext context) {
        XAttr endPointAttr = getAttr(XAttr.ATTR_ENDPOINT);
        String endPoint = Objects.isNull(endPointAttr) ? ConfGenerator.ENDPOINT_ALL : endPointAttr.getValue();
        if (!ConfGenerator.ENDPOINT_ALL.equals(endPoint) &&
                !ConfGenerator.ENDPOINT_ALL.equals(context.getEndPoint()) &&
                !context.getEndPoint().equals(endPoint)) {
            return null;
        }

        XField field = super.build0(context);
        ConfBeanField confField = (ConfBeanField) field;
        XAttr colFrom = getAttr(XAttr.ATTR_COLFROM);
        confField.setFromCol(Objects.isNull(colFrom) ? "" : colFrom.getValue());
        confField.setEndPoint(endPoint);
        XAttr foreignAttr = getAttr(XAttr.ATTR_FOREIGN_CHECK);
        if (Objects.nonNull(foreignAttr)) {
            confField.setForeignCheck(foreignAttr.getValue());
        }
        XAttr uniqAttr = getAttr(XAttr.ATTR_UNIQ_CHECK);
        if (Objects.nonNull(uniqAttr)) {
            confField.setUniqCheck(uniqAttr.getValue());
        }
        return confField;
    }
}
