package xlite.xml.attr;

import org.w3c.dom.Attr;
import xlite.type.TypeBuilder;
import xlite.type.XType;
import xlite.xml.element.XElement;

import java.util.Objects;
import java.util.function.Consumer;

public class TypeAttr extends AbsAttr {
    public TypeAttr(Attr src, XElement parent) {
        super(src, parent);
    }

    public XType build(XAttr keyAttr, XAttr valueAttr, Consumer<XType> cb) {
        String key = "", value = "";
        if (Objects.nonNull(keyAttr)) {
            key = keyAttr.getValue();
        }
        if (Objects.nonNull(valueAttr)) {
            value = valueAttr.getValue();
        }
        return TypeBuilder.build(getValue(), key, value, cb);
    }
}
