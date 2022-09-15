package xlite.conf;

import org.dom4j.DocumentException;
import xlite.coder.XPackage;
import xlite.gen.XGenerator;
import xlite.xml.XmlContext;
import xlite.xml.XParser;
import xlite.xml.element.PackageElement;
import xlite.xml.element.XElement;

import java.net.URL;

public class ConfGenerator {
    private final XParser parser;
    private final XGenerator generator;
    private final XmlContext context;

    public ConfGenerator(URL xml, String out, String language) throws DocumentException {
        ConfFactory factory = new ConfFactory();
        context = new XmlContext(factory);
        parser = new XParser(xml, context);
        generator = new XGenerator(out, language, factory);
    }

    public void gen(boolean isLoadCode) {
        XElement root = parser.parse();
        if (!(root instanceof PackageElement)) {
            throw new RuntimeException("conf xml root must be package");
        }
        context.setConfLoadCode(isLoadCode);
        PackageElement packageElement = (PackageElement) root;
        XPackage xPackage = packageElement.build(context);
        generator.gen(xPackage);
    }
}
