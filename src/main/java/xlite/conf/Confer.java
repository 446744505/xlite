package xlite.conf;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.FilenameUtils;
import xlite.conf.formatter.DataFormatter;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public abstract class Confer<K, V> {
    protected boolean isAllLoaded;
    private final List<Consumer<Map<K, V>>> onLoads = new ArrayList<>();

    public abstract Map<K, V> all();

    public Map<K, V> loadAll(File[] files) throws Exception {
        Map<K, V> rst = new HashMap<>();
        for (File file : files) {
            rst.putAll(loadOneFile(file));
        }
        isAllLoaded = true;
        return rst;
    }

    public Map<K, V> load(File[] files, K id) throws Exception {

        return loadOneFile(files[0]);
    }

    private Map<K, V> loadOneFile(File file) throws Exception {
        String ext = FilenameUtils.getExtension(file.getName());
        DataFormatter formatter = DataFormatter.createFormatter(ext);
        Map<K, V> rst = formatter.load(file, new TypeReference<Map<K, V>>() {});
        onLoad(rst);
        return rst;
    }

    public final void onLoad(Consumer<Map<K, V>> callback) {
        onLoads.add(callback);
    }

    public final void onLoad(Map<K, V> conf) {
        for (Consumer<Map<K, V>> onLoad : onLoads) {
            onLoad.accept(conf);
        }
    }
}
