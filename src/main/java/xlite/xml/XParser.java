package xlite.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import xlite.xml.element.XElement;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

public class XParser {
    private final XmlContext context;
    private final Document document;

    public XParser(URL url, XmlContext context) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(url);
        this.context = context;
    }

    public XParser(File file, XmlContext context) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(file);
        this.context = context;
    }

    public XParser(InputStream input, XmlContext context) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(input);
        this.context = context;
    }

    public XParser(String systemId, XmlContext context) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(systemId);
        this.context = context;
    }

    public XParser(Reader r, XmlContext context) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(r);
        this.context = context;
    }

    public XElement parse() {
        Element root = document.getRootElement();
        XElement xroot = context.getFactory().createElement(root, null);
        return xroot.parse(xroot, context);
    }
}
