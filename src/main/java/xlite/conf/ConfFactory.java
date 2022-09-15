package xlite.conf;

import org.dom4j.Attribute;
import org.dom4j.Element;
import xlite.coder.XCoder;
import xlite.coder.XField;
import xlite.conf.elem.ConfBeanElement;
import xlite.conf.elem.ConfVarElement;
import xlite.gen.GenConf;
import xlite.gen.SimpleGenFactory;
import xlite.gen.XGenFactory;
import xlite.language.XLanguage;
import xlite.type.XType;
import xlite.xml.SimpleXmlFactory;
import xlite.xml.XXmlFactory;
import xlite.xml.attr.XAttr;
import xlite.xml.element.XElement;

public class ConfFactory implements XXmlFactory, XGenFactory {
    private final XXmlFactory xmlFactory = new SimpleXmlFactory();
    private final XGenFactory genFactory = new SimpleGenFactory();

    @Override
    public XElement createElement(Element src, XElement parent) {
        String name = src.getName();
        if (XElement.ELEMENT_BEAN.equals(name)) {
            return new ConfBeanElement(src, parent);
        } else if (XElement.ELEMENT_VAR.equals(name)) {
            return new ConfVarElement(src, parent);
        }
        return xmlFactory.createElement(src, parent);
    }

    @Override
    public XAttr createAttr(Attribute src, XElement parent) {
        return xmlFactory.createAttr(src, parent);
    }

    @Override
    public XField createField(String name, XType type, XCoder parent) {
        return new ConfField(name, type, parent);
    }

    @Override
    public XLanguage createLanguage(String l) {
        return genFactory.createLanguage(l);
    }

    @Override
    public GenConf createConf() {
        return new ConfGenConf();
    }
}
