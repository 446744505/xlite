package xlite.type;

import java.util.*;
import java.util.function.Consumer;

public class TypeBuilder {
    public static final String TYPE_VOID = "void";
    public static final String TYPE_ANY = "any";
    public static final String TYPE_BOOL = "bool";
    public static final String TYPE_BYTE = "byte";
    public static final String TYPE_SHORT = "short";
    public static final String TYPE_INT = "int";
    public static final String TYPE_LONG = "long";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_DOUBLE = "double";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_LIST = "list";
    public static final String TYPE_MAP = "map";

    private static final Map<String, XType> baseTypes = new HashMap<>();
    private static final Map<String, XType> defineTypes = new HashMap<>();
    private static final Map<String, List<Consumer<XType>>> waitBuildType = new HashMap<>();

    public static final XVoid VOID = new XVoid();
    public static final XBool BOOL = new XBool();
    public static final XByte BYTE = new XByte();
    public static final XShort SHORT = new XShort();
    public static final XInt INT = new XInt();
    public static final XLong LONG = new XLong();
    public static final XFloat FLOAT = new XFloat();
    public static final XDouble DOUBLE = new XDouble();
    public static final XString STRING = new XString();

    static {
        baseTypes.put(TYPE_BOOL, BOOL);
        baseTypes.put(TYPE_BYTE, BYTE);
        baseTypes.put(TYPE_SHORT, SHORT);
        baseTypes.put(TYPE_INT, INT);
        baseTypes.put(TYPE_LONG, LONG);
        baseTypes.put(TYPE_STRING, STRING);
        baseTypes.put(TYPE_FLOAT, FLOAT);
        baseTypes.put(TYPE_DOUBLE, DOUBLE);
    }

    public static XType build(String type, String key, String value, Consumer<XType> cb) {
        if (TYPE_ANY.equals(type)) {
            return new XAny();
        }
        XType typ = baseTypes.get(type);
        if (Objects.nonNull(typ)) {
            return typ;
        }
        typ = defineTypes.get(type);
        if (Objects.nonNull(typ)) {
            return typ;
        }
        if (TYPE_LIST.equals(type)) {
            XList l = new XList();
            XType v = build(value, null, null, t -> l.setValue(t));
            l.setValue(v);
            return l;
        }
        if (TYPE_MAP.equals(type)) {
            XMap m = new XMap();
            XType k = build(key, null, null, t -> m.setKey(t));
            XType v = build(value, null, null, t -> m.setValue(t));
            m.setKey(k);
            m.setValue(v);
            return m;
        }

        List<Consumer<XType>> waits = waitBuildType.get(type);
        if (Objects.isNull(waits)) {
            waits = new ArrayList<>();
            waitBuildType.put(type, waits);
        }
        waits.add(cb);
        return null;
    }

    public static void registerBean(XBean bean) {
        String name = bean.getName();
        if (null != defineTypes.put(name, bean)) {
            throw new IllegalStateException(String.format("type of repetition:", name));
        }
        List<Consumer<XType>> waits = waitBuildType.get(name);
        if (Objects.nonNull(waits)) {
            waits.forEach(cb -> cb.accept(bean));
        }
    }
}
