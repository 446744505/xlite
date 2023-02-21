package xlite.conf;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SplitMeta<K extends Comparable<K>> {
    public static final String META_FILE = "_meta";

    @Getter private final List<Index<K>> indexs = new ArrayList<>();

    public void addIndex(Object start, Object end, int index) {
        Index i = new Index();
        i.index = index;
        i.start = (Comparable) start;
        i.end = (Comparable) end;
        indexs.add(i);
    }

    public int findIndex(K id) {
        for (Index<K> index : indexs) {
            if (id.compareTo(index.start) >= 0
                && id.compareTo(index.end) <= 0) {
                return index.index;
            }
        }
        return 0;
    }

    public static class Index<K extends Comparable<K>> {
        public int index;
        public K start;
        public K end;
    }
}
