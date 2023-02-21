package xlite.conf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.util.ConcurrentHashSet;
import xlite.conf.formatter.DataFormatter;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public abstract class Confer<K extends Comparable<K>, V> {
    protected boolean isAllLoaded;
    private SplitMeta<K> meta;
    private final Set<Integer> loadedIndex = new ConcurrentHashSet<>();
    private final List<Consumer<Map<K, V>>> onLoads = new ArrayList<>();

    public abstract Map<K, V> all();
    public abstract V one(K id);

    public void freshIsLoadAll() {
        if (!isAllLoaded && Objects.nonNull(meta)) {
            isAllLoaded = loadedIndex.size() == meta.getIndexs().size();
        }
    }

    public void reset() {
        meta = null;
        isAllLoaded = false;
        loadedIndex.clear();
    }

    public Map<K, V> loadAll(File[] files, TypeReference ref) throws Exception {
        Map<K, V> rst = new TreeMap<>();
        for (File file : files) {
            rst.putAll(loadOneFile(file, ref));
        }
        isAllLoaded = true;
        return rst;
    }

    public Map<K, V> load(File[] files, K id, TypeReference dataRef, TypeReference metaRef) throws Exception {
        if (files.length == 1) {
            return loadAll(files, dataRef);
        }
        if (Objects.isNull(meta)) {
            for (File file : files) {
                if (FilenameUtils.getBaseName(file.getName()).endsWith(SplitMeta.META_FILE)) {
                    loadMeta(file, metaRef);
                    break;
                }
            }
        }
        int index = meta.findIndex(id);
        if (index == 0) {
            return Collections.EMPTY_MAP;
        }

        for (File file : files) {
            if (FilenameUtils.getBaseName(file.getName()).endsWith("_" + index)) {
                loadedIndex.add(index);
                return loadOneFile(file, dataRef);
            }
        }
        return Collections.EMPTY_MAP;
    }

    private void loadMeta(File metaFile, TypeReference ref) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        meta = (SplitMeta<K>) mapper.readValue(metaFile, ref);
    }

    private Map<K, V> loadOneFile(File file, TypeReference ref) throws Exception {
        String baseName = FilenameUtils.getBaseName(file.getName());
        if (baseName.endsWith(SplitMeta.META_FILE)) {
            return Collections.EMPTY_MAP;
        }
        String ext = FilenameUtils.getExtension(file.getName());
        DataFormatter formatter = DataFormatter.createFormatter(ext);
        Map<K, V> rst = formatter.load(file, ref);
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
