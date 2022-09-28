package xlite.conf;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import xlite.coder.*;
import xlite.conf.elem.ConfBeanElement;
import xlite.conf.elem.ConfBeanVarElement;
import xlite.conf.elem.ConfEnumElement;
import xlite.conf.elem.ConfEnumVarElement;
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
        String name = src.getNodeName();
        if (XElement.ELEMENT_BEAN.equals(name)) {
            return new ConfBeanElement(src, parent);
        } else if (XElement.ELEMENT_VAR.equals(name)) {
            if (parent instanceof ConfEnumElement) {
                return new ConfEnumVarElement(src, parent);
            }
            return new ConfBeanVarElement(src, parent);
        } else if (XElement.ELEMENT_ENUM.equals(name)) {
            return new ConfEnumElement(src, parent);
        }
        return xmlFactory.createElement(src, parent);
    }

    @Override
    public XAttr createAttr(Attr src, XElement parent) {
        return xmlFactory.createAttr(src, parent);
    }

    @Override
    public XField createField(String name, XType type, XCoder parent) {
        return new ConfBeanField(name, type, parent);
    }

    @Override
    public XEnumField createEnumField(String name, XType type, XCoder parent) {
        return new ConfEnumField(name, type, parent);
    }

    @Override
    public XClass createClass(String name, XCoder parent) {
        return new ConfClass(name, parent);
    }

    @Override
    public XEnumer createEnum(String name, XCoder parent) {
        return new ConfEnum(name, parent);
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
