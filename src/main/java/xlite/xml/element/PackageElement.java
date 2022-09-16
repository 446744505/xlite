package xlite.xml.element;

import org.dom4j.Element;
import xlite.coder.XPackage;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackageElement extends AbsElement {
    private PackageElement parent;
    private List<PackageElement> children = new ArrayList<>();

    public PackageElement(Element element, XElement parent) {
        super(element, parent);
    }

    @Override
    public XElement parse(XElement preEle, XmlContext context) {
        super.parse(preEle, context);
        elements.stream()
                .filter(ele -> ele instanceof PackageElement)
                .map(ele -> (PackageElement) ele)
                .forEach(ele -> {
                    ele.parent = this;
                    children.add(ele);
                });
        return this;
    }

    @Override
    protected boolean checkAttr(String name) {
        return XAttr.ATTR_NAME.equals(name);
    }

    @Override
    public XPackage build0(XmlContext context) {
        XPackage parentPak = null;
        if (!Objects.isNull(parent)) {
            parentPak = parent.build(context);
        }
        XAttr nameAttr = getAttr(XAttr.ATTR_NAME);
        XPackage pak = setCache(new XPackage(nameAttr.getValue(), parentPak));
        elements.stream()
                .filter(ele -> ele instanceof BeanElement)
                .map(ele -> (BeanElement) ele)
                .forEach(ele -> pak.addClass(ele.build(context)));
        elements.stream()
                .filter(ele -> ele instanceof EnumElement)
                .map(ele -> (EnumElement) ele)
                .forEach(ele -> pak.addEnum(ele.build(context)));
        children.forEach(child -> pak.addChild(child.build(context)));
        return pak;
    }
}
