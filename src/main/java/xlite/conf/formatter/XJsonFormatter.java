package xlite.conf.formatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * json for sql select
 */
public class XJsonFormatter extends DataFormatter {
    @Override
    public void export(Map<?, ?> conf, Class clazz, File outDir) throws Exception {
        File file = new File(outDir, clazz.getSimpleName() + ".xjson");
        FileWriter writer = new FileWriter(file);
        for (Object val : conf.values()) {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(val);
            writer.append(json).append("\n");
        }
        writer.flush();
    }

    @Override
    public <K, V> Map<K, V> load(File file, TypeReference<Map<K, V>> ref) throws Exception {
        return null;
    }
}
