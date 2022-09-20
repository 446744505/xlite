package xlite.conf.formatter;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.util.Map;

public class XmlFormatter extends DataFormatter {
    protected XmlFormatter(File dir) {
        super(dir);
    }

    @Override
    public void export(Map<?, ?> conf, Class clazz) throws Exception {
        XmlMapper mapper = new XmlMapper();
        File file = new File(dir, clazz.getSimpleName() + ".xml");
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, conf);
    }
}
