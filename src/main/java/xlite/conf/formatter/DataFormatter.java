package xlite.conf.formatter;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public abstract class DataFormatter {
    public abstract void export(Map<?, ?> conf, String fileName, File outDir) throws Exception;
    public abstract <K, V> Map<K, V> load(File file, TypeReference<TreeMap<K, V>> ref) throws Exception;

    public static DataFormatter createFormatter(String formatter) {
        if ("json".equals(formatter)) {
            return new JsonFormatter();
        } else if ("xml".equals(formatter)) {
            return new XmlFormatter();
        } else if ("xjson".equals(formatter)) {
            return new XJsonFormatter();
        }
        throw new UnsupportedOperationException("unsupported data output format " + formatter);
    }
}
