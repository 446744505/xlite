package xlite.conf;

import lombok.Getter;
import xlite.CheckException;
import xlite.excel.Reader;
import xlite.util.Util;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.*;

public class ForeignCheck {
    @Getter private final Class owner;
    @Getter private final String ownerField;
    @Getter private final Class checker;
    @Getter private final String checkField;
    @Getter private final Object val;
    @Getter private final boolean checkChild;

    private static final List<ForeignCheck> foreignChecks = new ArrayList<>();

    public ForeignCheck(Class owner, String ownerField, Class checker, String checkField, Object val, boolean checkChild) {
        this.owner = owner;
        this.ownerField = ownerField;
        this.checker = checker;
        this.checkField = checkField;
        this.val = val;
        this.checkChild = checkChild;
    }

    public static void addForeignCheck(Reader obj, String ownerField, Class checker, String checkField, Object val, boolean checkChild) {
        if (val instanceof Collection) {
            Collection values = (Collection) val;
            values.forEach(v -> addForeignCheck(obj, ownerField, checker, checkField, v, checkChild));
        } else {
            foreignChecks.add(new ForeignCheck(obj.getClass(), ownerField, checker, checkField, val, checkChild));
        }
    }

    public static void doForeignCheck(ConfGenerator generator) throws Exception {
        for (ForeignCheck check : foreignChecks) {
            boolean pass = false;
            for (Map.Entry<Class, Map<Object, Object>> en : generator.getAllConf().entrySet()) {
                if (pass) break;
                Class clazz = en.getKey();
                Class checker = check.getChecker();
                if (clazz == checker || (check.isCheckChild() && Util.isChild(clazz, checker))) {
                    Map<Object, Object> datas = en.getValue();
                    for (Object obj : datas.values()) {
                        Class objClass = obj.getClass();
                        Field field = Util.getField(objClass, check.getCheckField());
                        if (Objects.isNull(field)) {
                            throw new FileNotFoundException(String.format("there is no field %s at bean %s",
                                    check.getCheckField(), objClass.getName()));
                        }
                        field.setAccessible(true);
                        Object val = field.get(obj);
                        if (val.equals(check.getVal())) {
                            pass = true;
                            break;
                        }
                    }
                }
            }
            if (!pass) {
                throw new CheckException(String.format("%s`s field %s value %s not in %s.%s",
                        check.getOwner().getName(), check.getOwnerField(), check.getVal(), check.getChecker().getName(), check.getCheckField()));
            }
        }
    }
}
