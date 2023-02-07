package xlite.conf;

import xlite.CheckException;
import xlite.util.Util;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 检查checker表的某列的值必须在owner表某列里出现
 */
public class MustCheck {
    private final Class owner;
    private final String ownerField;
    private final Class checker;
    private final String checkField;

    private static List<MustCheck> mustChecks = new ArrayList<>();

    public MustCheck(Class owner, String ownerField, Class checker, String checkField) {
        this.owner = owner;
        this.ownerField = ownerField;
        this.checker = checker;
        this.checkField = checkField;
    }

    public static void addMustCheck(Class owner, String ownerField, Class checker, String checkField) {
        mustChecks.add(new MustCheck(owner, ownerField, checker, checkField));
    }

    public static void doMustCheck(ConfGenerator generator) throws Exception {
        for (MustCheck check : mustChecks) {
            Set<Object> allConf = new HashSet<>();
            for (Object conf : generator.getAllConf().get(check.checker).values()) {
                Field field = Util.getField(check.checker, check.checkField);
                if (Objects.isNull(field)) {
                    throw new FileNotFoundException(String.format("there is no field %s at bean %s",
                            check.checkField, check.checker.getName()));
                }
                field.setAccessible(true);
                Object val = field.get(conf);
                allConf.add(val);
            }
            for (Object conf : generator.getAllConf().get(check.owner).values()) {
                Field field = Util.getField(check.owner, check.ownerField);
                if (Objects.isNull(field)) {
                    throw new FileNotFoundException(String.format("there is no field %s at bean %s",
                            check.ownerField, check.owner.getName()));
                }
                field.setAccessible(true);
                Object val = field.get(conf);
                allConf.remove(val);
            }
            if (!allConf.isEmpty()) {
                throw new CheckException(String.format("%s`s field %s values %s must exist at %s.%s",
                        check.checker, check.checkField, allConf, check.owner.getName(), check.ownerField));
            }
        }
    }
}
