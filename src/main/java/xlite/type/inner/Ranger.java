package xlite.type.inner;

public interface Ranger<T extends Comparable<T>> {
    Range<T> create(String min, String max, boolean leftOpen, boolean rightOpen);
}
