package xlite.conf;

import java.util.*;
import java.util.function.Function;

public class Index<K, V> {
    private final Function<V, K> getKey;
    private Map<K, List<V>> confs = new HashMap<>();

    public Index(Function<V, K> getKey) {
        this.getKey = getKey;
    }

    public void index(Map<?, V> conf) {
        Map<K, List<V>> cfgs = new HashMap<>();
        conf.forEach((id, val) -> {
            K k = getKey.apply(val);
            List<V> list = cfgs.get(k);
            if (list == null) {
                list = new ArrayList<>();
                cfgs.put(k, list);
            }
            list.add(val);
        });

        Map<K, List<V>> unmodify = new HashMap<>();
        cfgs.forEach((k, list) -> unmodify.put(k, Collections.unmodifiableList(list)));
        this.confs = Collections.unmodifiableMap(unmodify);
    }

    public List<V> one(K index) {
        return confs.get(index);
    }

    public Map<K, List<V>> all() {
        return confs;
    }
}
