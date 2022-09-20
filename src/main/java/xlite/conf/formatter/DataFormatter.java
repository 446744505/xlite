package xlite.conf.formatter;

import java.io.File;
import java.util.Map;

public abstract class DataFormatter {
    protected final File dir;

    protected DataFormatter(File dir) {
        this.dir = dir;
    }

    public abstract void export(Map<?, ?> conf, Class clazz) throws Exception;

    public static DataFormatter createFormatter(String formatter, File dir) {
        if ("json".equals(formatter)) {
            return new JsonFormatter(dir);
        } else if ("xml".equals(formatter)) {
            return new XmlFormatter(dir);
        }
        throw new UnsupportedOperationException(formatter);
    }
}
