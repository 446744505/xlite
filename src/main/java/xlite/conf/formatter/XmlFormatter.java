package xlite.conf.formatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.util.Map;

public class XmlFormatter extends DataFormatter {

    @Override
    public void export(Map<?, ?> conf, Class clazz, File outDir) throws Exception {
        XmlMapper mapper = new XmlMapper();
        File file = new File(outDir, clazz.getSimpleName() + ".xml");
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, conf);
    }

    @Override
    public <K, V> Map<K, V> load(File file, TypeReference<Map<K, V>> ref) throws Exception {
        XmlMapper mapper = new XmlMapper();
        return (Map<K, V>) mapper.readValue(file, ref);
    }
}
