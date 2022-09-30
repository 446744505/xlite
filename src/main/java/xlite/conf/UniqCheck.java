package xlite.conf;

import xlite.CheckException;

import java.util.*;

public class UniqCheck {
    private static final Map<Class, Map<String, Set<Object>>> allUniqVal = new HashMap<>();

    public static void check(Class clazz, String fieldName, Object val) throws CheckException {
        Map<String, Set<Object>> fields = allUniqVal.get(clazz);
        if (Objects.isNull(fields)) {
            fields = new HashMap<>();
            allUniqVal.put(clazz, fields);
        }
        Set<Object> values = fields.get(fieldName);
        if (Objects.isNull(values)) {
            values = new HashSet<>();
            fields.put(fieldName, values);
        }
        if (!values.add(val)) {
            throw new CheckException(String.format("bean %s`s field %s value %s not unique", clazz.getName(), fieldName, val));
        }
    }
}
