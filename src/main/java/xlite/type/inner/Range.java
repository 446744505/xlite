package xlite.type.inner;

import xlite.coder.XClass;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.util.Util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Range<T extends Comparable<T>> {
    private static final Range FOREVER_TRUE = new Range(null, null, false, false);
    private static final Map<String, Ranger> handler = new HashMap<>();

    static {
        register0(TypeBuilder.TYPE_BYTE, Byt.INSTANCE);
        register0(TypeBuilder.TYPE_SHORT, Srt.INSTANCE);
        register0(TypeBuilder.TYPE_INT, Int.INSTANCE);
        register0(TypeBuilder.TYPE_LONG, Lng.INSTANCE);
        register0(TypeBuilder.TYPE_FLOAT, Flt.INSTANCE);
        register0(TypeBuilder.TYPE_DOUBLE, Dub.INSTANCE);
        register0(TypeBuilder.TYPE_DATE, Date.INSTANCE);
    }

    private final T min;
    private final T max;
    private final boolean rightOpen;
    private final boolean leftOpen;

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
        if (this == FOREVER_TRUE) {
            return true;
        }

        boolean pass;
        if (Objects.isNull(min)) {
            pass = true;
        } else {
            int c = v.compareTo(min);
            if (leftOpen) {
                pass = c > 0;
            } else {
                pass = c > 0 || c == 0;
            }
        }

        if (!pass) {
            return false;
        }

        if (Objects.nonNull(max)) {
            int c = v.compareTo(max);
            if (rightOpen) {
                pass = c < 0;
            } else {
                pass = c < 0 || c == 0;
            }
        }

        return pass;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public boolean isRightOpen() {
        return rightOpen;
    }

    public boolean isLeftOpen() {
        return leftOpen;
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

    /**
     * 可以注册自己的range类型
     * @param clazz
     * @param ranger
     */
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
            return FOREVER_TRUE;
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
        String min = arr[0].substring(1).trim();
        String max = arr[1].substring(0, arr[1].length() - 1).trim();
        return ranger.create(min, max, leftOpen, rightOpen);
    }

    private static class Int implements Ranger<Integer> {
        private static final Int INSTANCE = new Int();
        @Override
        public Range<Integer> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Integer m1 = Util.isEmpty(min) ? null : Integer.valueOf(min);
            Integer m2 = Util.isEmpty(max) ? null : Integer.valueOf(max);
            if (Objects.nonNull(m1) && Objects.nonNull(m2) && m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class Byt implements Ranger<Byte> {
        private static final Byt INSTANCE = new Byt();
        @Override
        public Range<Byte> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Byte m1 = Util.isEmpty(min) ? null : Byte.valueOf(min);
            Byte m2 = Util.isEmpty(max) ? null : Byte.valueOf(max);
            if (Objects.nonNull(m1) && Objects.nonNull(m2) && m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class Srt implements Ranger<Short> {
        private static final Srt INSTANCE = new Srt();
        @Override
        public Range<Short> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Short m1 = Util.isEmpty(min) ? null : Short.valueOf(min);
            Short m2 = Util.isEmpty(max) ? null : Short.valueOf(max);
            if (Objects.nonNull(m1) && Objects.nonNull(m2) && m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class Lng implements Ranger<Long> {
        private static final Lng INSTANCE = new Lng();
        @Override
        public Range<Long> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Long m1 = Util.isEmpty(min) ? null : Long.valueOf(min);
            Long m2 = Util.isEmpty(max) ? null : Long.valueOf(max);
            if (Objects.nonNull(m1) && Objects.nonNull(m2) && m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class Flt implements Ranger<Float> {
        private static final Flt INSTANCE = new Flt();
        @Override
        public Range<Float> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Float m1 = Util.isEmpty(min) ? null : Float.valueOf(min);
            Float m2 = Util.isEmpty(max) ? null : Float.valueOf(max);
            if (Objects.nonNull(m1) && Objects.nonNull(m2) && m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
    private static class Dub implements Ranger<Double> {
        private static final Dub INSTANCE = new Dub();
        @Override
        public Range<Double> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            Double m1 = Util.isEmpty(min) ? null : Double.valueOf(min);
            Double m2 = Util.isEmpty(max) ? null : Double.valueOf(max);
            if (Objects.nonNull(m1) && Objects.nonNull(m2) && m1 > m2) {
                throw new IllegalArgumentException("min > max : " + min + " > " + max);
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }

    private static class Date implements Ranger<DateTime> {
        private static final Date INSTANCE = new Date();
        @Override
        public Range<DateTime> create(String min, String max, boolean leftOpen, boolean rightOpen) {
            DateTime m1 = Util.isEmpty(min) ? null : new DateTime(min);
            DateTime m2 = Util.isEmpty(max) ? null : new DateTime(max);
            if (Objects.nonNull(m1) && Objects.nonNull(m2)) {
                LocalDateTime l1 = m1.asLocalDateTime();
                LocalDateTime l2 = m2.asLocalDateTime();
                if (l1.isAfter(l2)) {
                    throw new IllegalArgumentException("min > max : " + min + " > " + max);
                }
            }
            return new Range(m1, m2, leftOpen, rightOpen);
        }
    }
}
