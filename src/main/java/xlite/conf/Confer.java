package xlite.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Confer<K, V> {
    private final List<Consumer<Map<K, V>>> onLoads = new ArrayList<>();

    public abstract Map<K, V> all();
    public abstract V one(K id);

    public final void onLoad(Consumer<Map<K, V>> callback) {
        onLoads.add(callback);
    }

    public final void onLoad(Map<K, V> conf) {
        for (Consumer<Map<K, V>> onLoad : onLoads) {
            onLoad.accept(conf);
        }
    }
}
