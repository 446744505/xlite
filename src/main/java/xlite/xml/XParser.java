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
    private final XXmlFactory factory;
    private final Document document;

    public XParser(URL url, XXmlFactory factory) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(url);
        this.factory = factory;
    }

    public XParser(File file, XXmlFactory factory) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(file);
        this.factory = factory;
    }

    public XParser(InputStream input, XXmlFactory factory) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(input);
        this.factory = factory;
    }

    public XParser(String systemId, XXmlFactory factory) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(systemId);
        this.factory = factory;
    }

    public XParser(Reader r, XXmlFactory factory) throws DocumentException {
        SAXReader reader = new SAXReader();
        document = reader.read(r);
        this.factory = factory;
    }

    public XElement parse() {
        Element root = document.getRootElement();
        XElement xroot = factory.createElement(root, null);
        return xroot.parse(factory);
    }
}
