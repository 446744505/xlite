package xlite.conf.formatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class JsonFormatter extends DataFormatter {

    @Override
    public void export(Map<?, ?> conf, String fileName, File outDir) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(outDir, fileName + ".json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, conf);
    }

    @Override
    public <K, V> Map<K, V> load(File file, TypeReference<Map<K, V>> ref) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return (Map<K, V>) mapper.readValue(file, ref);
    }
}
