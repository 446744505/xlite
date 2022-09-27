package xlite.type.inner;

import lombok.Getter;
import xlite.coder.XClass;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Range<T extends Comparable<T>> {
    private static final Range ALLTRUE = new Range(null, null, false, false);
    private static final Map<String, Ranger> handler = new HashMap<>();

    static {
        register0(TypeBuilder.TYPE_BYTE, B.INSTANCE);
        register0(TypeBuilder.TYPE_SHORT, S.INSTANCE);
        register0(TypeBuilder.TYPE_INT, I.INSTANCE);
        register0(TypeBuilder.TYPE_LONG, L.INSTANCE);
        register0(TypeBuilder.TYPE_FLOAT, F.INSTANCE);
        register0(TypeBuilder.TYPE_DOUBLE, D.INSTANCE);
    }

    @Getter private final T min;
    @Getter private final T max;
    @Getter private final boolean rightOpen;
    @Getter private final boolean leftOpen;

    public Range(T min, T max, boolean leftOpen, boolean rigthOpen) {
        this.min = min;
        this.max = max;
        this.leftOpen = leftOpen;
        this.rightOpen = rigthOpen;
    }

    public Range() { //for decode
        min = null;
        max = null;
        rightOpen = false;
        leftOpen = false;
    }

    public boolean check(T v) {
        if (this == ALLTRUE) {
            return true;
        }

        boolean pass;
        int c = v.compareTo(min);
        if (leftOpen) {
            pass = c > 0;
        } else {
            pass = c > 0 || c == 0;
        }

        if (!pass) {
            return false;
        }

        c = v.compareTo(max);
        if (rightOpen) {
            pass = c < 0;
        } else {
            pass = c < 0 || c == 0;
        }

        return pass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, leftOpen, rightOpen);
    }

    @Override
    public String toString() {
        return (leftOpen ? "(" : "[") + min + "," + max + (rightOpen ? ")" : "]");
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj == this) return true;
        if (_obj instanceof Range) {
            Range<?> _other = (Range<?>) _obj;
            if (!this.min.equals(_other.min)) return false;
            if (!this.max.equals(_other.max)) return false;
            return true;
        }
        return false;
    }

    private static void register0(String type, Ranger ranger) {
        handler.put(type, ranger);
    }

    public static void register(Class clazz, Ranger ranger) {
        handler.put(clazz.getSimpleName(), ranger);
        TypeBuilder.registerBean(new XBean(clazz));
        XClass.register(clazz);
    }

    public static <T extends Comparable<T>> Range<T> toRange(String valType, String format) {
        Ranger ranger = handler.get(valType);
        if (Objects.isNull(ranger)) {
            throw new UnsupportedOperationException("unsupported range value type " + valType);
        }
        if (Util.isEmpty(format)) {
            return ALLTRUE;
        }

        boolean rightOpen = false;
        boolean leftOpen = false;
        if (format.startsWith("(")) {
            leftOpen = true;
        }
        if (format.endsWith(")")) {
            rightOpen = true;
        }
        String[] arr = format.split(",");
        String min = arr[0].substring(1, arr[0].length()).trim();
        String max = arr[1].substring(0, arr[1].length() - 1).trim();
        return ranger.create(min, max, leftOpen, rightOpen);
    }

    private static class I implements Ranger<Integer> {
        private static final I INSTANCE = new I();
        @Override
        public Range<Integer> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Integer m1 = Integer.valueOf(min);
            Integer m2 = Integer.valueOf(max);
            if (m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class B implements Ranger<Byte> {
        private static final B INSTANCE = new B();
        @Override
        public Range<Byte> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Byte m1 = Byte.valueOf(min);
            Byte m2 = Byte.valueOf(max);
            if (m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class S implements Ranger<Short> {
        private static final S INSTANCE = new S();
        @Override
        public Range<Short> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Short m1 = Short.valueOf(min);
            Short m2 = Short.valueOf(max);
            if (m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class L implements Ranger<Long> {
        private static final L INSTANCE = new L();
        @Override
        public Range<Long> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Long m1 = Long.valueOf(min);
            Long m2 = Long.valueOf(max);
            if (m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class F implements Ranger<Float> {
        private static final F INSTANCE = new F();
        @Override
        public Range<Float> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Float m1 = Float.valueOf(min);
            Float m2 = Float.valueOf(max);
            if (m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class D implements Ranger<Double> {
        private static final D INSTANCE = new D();
        @Override
        public Range<Double> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Double m1 = Double.valueOf(min);
            Double m2 = Double.valueOf(max);
            if (m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
}
