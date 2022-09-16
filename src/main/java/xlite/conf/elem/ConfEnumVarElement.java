package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XField;
import xlite.conf.ConfEnumField;
import xlite.xml.XmlContext;
import xlite.xml.attr.SimpleAttr;
import xlite.xml.attr.XAttr;
import xlite.xml.element.EnumVarElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfEnumVarElement extends EnumVarElement {
    public ConfEnumVarElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    protected boolean checkAttr(String name) {
        if (super.checkAttr(name)) {
            return true;
        }
        return XAttr.ATTR_COLFROM.equals(name) ||
                XAttr.ATTR_COMMENT.equals(name);
    }

    @Override
    public ConfEnumField build0(XmlContext context) {
        XField field = super.build0(context);
        ConfEnumField confEnumField = (ConfEnumField) field;
        SimpleAttr commentAttr = getAttr(XAttr.ATTR_COMMENT);
        if (!Objects.isNull(commentAttr)) {
            field.setComment(commentAttr.getValue());
        }
        return confEnumField;
    }
}
