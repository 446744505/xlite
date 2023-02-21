package xlite.conf.formatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class XmlFormatter extends DataFormatter {

    @Override
    public void export(Map<?, ?> conf, String fileName, File outDir) throws Exception {
        XmlMapper mapper = new XmlMapper();
        File file = new File(outDir, fileName + ".xml");
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, conf);
    }

    @Override
    public <K, V> Map<K, V> load(File file, TypeReference<TreeMap<K, V>> ref) throws Exception {
        XmlMapper mapper = new XmlMapper();
        return (Map<K, V>) mapper.readValue(file, ref);
    }
}
