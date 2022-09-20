package xlite.conf.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class JsonFormatter extends DataFormatter {
    protected JsonFormatter(File dir) {
        super(dir);
    }

    @Override
    public void export(Map<?, ?> conf, Class clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(dir, clazz.getSimpleName() + ".json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, conf);
    }
}
