package xlite.xml.attr;

import org.dom4j.Attribute;
import xlite.type.TypeBuilder;
import xlite.type.XType;
import xlite.xml.element.XElement;


import java.util.Objects;
import java.util.function.Consumer;

public class TypeAttr extends AbsAttr {
    public TypeAttr(Attribute src, XElement parent) {
        super(src, parent);
    }

    public XType build(KeyAttr keyAttr, ValueAttr valueAttr, Consumer<XType> cb) {
        String key = "", value = "";
        if (!Objects.isNull(keyAttr)) {
            key = keyAttr.getValue();
        }
        if (!Objects.isNull(valueAttr)) {
            value = valueAttr.getValue();
        }
        return TypeBuilder.build(getValue(), key, value, cb);
    }
}
