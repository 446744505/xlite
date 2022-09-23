package xlite.xml.element;

import org.w3c.dom.Element;
import xlite.coder.XEnumer;
import xlite.coder.XField;
import xlite.type.*;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.Objects;

public class EnumElement extends AbsElement {
    public EnumElement(Element src, XElement parent) {
        super(src, parent);
    }

    @Override
    public XEnumer build0(XmlContext context) {
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        if (Objects.isNull(nameAttr)) {
            throw new NullPointerException("enum must have a name attr");
        }
        String name = nameAttr.getValue();
        XEnumer e = setCache(context.getFactory().createEnum(name, parent.build(context)));
        elements.stream()
                .filter(ele -> ele instanceof VarElement)
                .map(ele -> (VarElement) ele)
                .forEach(ele -> e.addField(ele.build(context)));
        TypeBase inner = null;
        for (XField f : e.getFields()) {
            XType ft = f.getType();
            if (ft instanceof XAny) {
                break;
            }
            if (!(ft instanceof TypeBase)) {
                throw new IllegalStateException("enum`s field type must base type at " + name);
            }
            if (Objects.isNull(inner)) {
                inner = (TypeBase) ft;
            }
            if (!ft.getClass().getName().equals(inner.getClass().getName())) {
                inner = null;
                break;
            }
        }
        TypeBuilder.registerBean(new XEnum(e.getName(), inner));
        return e;
    }

    public String getName() {
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        return nameAttr.getValue();
    }
}
