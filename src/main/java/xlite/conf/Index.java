package xlite.conf;

import java.util.*;
import java.util.function.Function;

public class Index<I, V> {
    private final Function<V, I> getKey;
    private Map<I, List<V>> confs = new HashMap<>();

    public Index(Function<V, I> getKey) {
        this.getKey = getKey;
    }

    public void index(Map<?, V> conf) {
        Map<I, List<V>> cfgs = new HashMap<>();
        conf.forEach((id, val) -> {
            I i = getKey.apply(val);
            List<V> list = cfgs.get(i);
            if (list == null) {
                list = new ArrayList<>();
                cfgs.put(i, list);
            }
            list.add(val);
        });

        Map<I, List<V>> unmodify = new HashMap<>();
        cfgs.forEach((i, list) -> unmodify.put(i, Collections.unmodifiableList(list)));
        this.confs = Collections.unmodifiableMap(unmodify);
    }

    public List<V> one(I index) {
        return confs.get(index);
    }

    public Map<I, List<V>> all() {
        return confs;
    }
}
