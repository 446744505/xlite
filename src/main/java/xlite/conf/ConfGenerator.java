package xlite.conf;

import org.dom4j.DocumentException;
import xlite.coder.XPackage;
import xlite.gen.XGenerator;
import xlite.xml.XParser;
import xlite.xml.element.PackageElement;
import xlite.xml.element.XElement;

import java.net.URL;

public class ConfGenerator {
    private final XParser parser;
    private final XGenerator generator;

    public ConfGenerator(URL xml, String out, String language) throws DocumentException {
        ConfFactory factory = new ConfFactory();
        parser = new XParser(xml, factory);
        generator = new XGenerator(out, language, factory);
    }

    public void gen() {
        XElement root = parser.parse();
        if (!(root instanceof PackageElement)) {
            throw new RuntimeException("conf xml root must be package");
        }
        PackageElement packageElement = (PackageElement) root;
        XPackage xPackage = packageElement.build();
        generator.gen(xPackage);
    }
}
