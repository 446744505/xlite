package xlite.conf.formatter;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.Map;

public abstract class DataFormatter {
    public abstract void export(Map<?, ?> conf, Class clazz, File outDir) throws Exception;
    public abstract <K, V> Map<K, V> load(File file, TypeReference<Map<K, V>> ref) throws Exception;

    public static DataFormatter createFormatter(String formatter) {
        if ("json".equals(formatter)) {
            return new JsonFormatter();
        } else if ("xml".equals(formatter)) {
            return new XmlFormatter();
        }
        throw new UnsupportedOperationException(formatter);
    }
}
