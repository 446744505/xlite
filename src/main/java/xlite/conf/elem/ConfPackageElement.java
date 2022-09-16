package xlite.conf.elem;

import org.dom4j.Element;
import xlite.coder.XPackage;
import xlite.xml.XmlContext;
import xlite.xml.element.EnumElement;
import xlite.xml.element.PackageElement;
import xlite.xml.element.XElement;

import java.util.Objects;

public class ConfPackageElement extends PackageElement {
    public ConfPackageElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public XPackage build(XmlContext context) {
        if (!Objects.isNull(buildPak)) {
            return buildPak;
        }
        XPackage pak = super.build(context);
        elements.stream()
                .filter(ele -> ele instanceof EnumElement)
                .map(ele -> (EnumElement) ele)
                .forEach(ele -> pak.addEnum(ele.build(context)));
        return pak;
    }
}
