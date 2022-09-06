package xlite.type;

import java.util.*;
import java.util.function.Consumer;

public class TypeBuilder {
    private static final String TYPE_INT = "int";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_LONG = "long";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_LIST = "list";
    private static final String TYPE_MAP = "map";

    private static final Map<String, XType> baseTypes = new HashMap<>();
    private static final Map<String, XType> defineTypes = new HashMap<>();
    private static final Map<String, List<Consumer<XType>>> waitBuildType = new HashMap<>();

    static {
        baseTypes.put(TYPE_INT, new XInt());
        baseTypes.put(TYPE_STRING, new XString());
        baseTypes.put(TYPE_FLOAT, new XFloat());
    }

    public static XType build(String type, String key, String value, Consumer<XType> cb) {
        XType typ = baseTypes.get(type);
        if (!Objects.isNull(typ)) {
            return typ;
        }
        typ = defineTypes.get(type);
        if (!Objects.isNull(typ)) {
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
        if (!Objects.isNull(waits)) {
            waits.forEach(cb -> cb.accept(bean));
        }
    }
}
