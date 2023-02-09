package xlite.xml;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xlite.xml.element.XElement;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XParser {
    private final File rootXml;
    private final XmlContext context;
    private final Document document;

    public XParser(File file, XmlContext context) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setXIncludeAware(true);
        factory.setNamespaceAware(true);
        document = factory.newDocumentBuilder().parse(file);
        this.context = context;
        this.rootXml = file;
    }

    public XElement parse() {
        Element root = document.getDocumentElement();
        XElement xroot = context.getFactory().createElement(root, null);
        return xroot.parse(xroot, context);
    }

    public File[] getAllXmls() {
        File dir = rootXml.getParentFile();
        return dir.listFiles((d, name) -> FilenameUtils.getExtension(name).equals("xml"));
    }
}
