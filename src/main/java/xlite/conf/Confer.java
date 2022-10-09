package xlite.conf;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Confer<K, V> {
    private java.util.function.Consumer<Map<K, V>> onLoad;

    public abstract Map<K, V> all();
    public abstract V one(K id);

    public final void onLoad(Consumer<Map<K, V>> callback) {
        onLoad = callback;
    }

    public final void onLoad(Map<K, V> conf) {
        if (Objects.nonNull(onLoad)) {
            onLoad.accept(conf);
        }
    }
}
