package xlite.coder;

import lombok.Getter;
import lombok.Setter;
import xlite.gen.GenConf;
import xlite.gen.GenContext;
import xlite.gen.visitor.*;
import xlite.language.XLanguage;
import xlite.type.XBean;
import xlite.type.XList;
import xlite.type.XMap;
import xlite.type.XType;

import java.util.*;
import java.util.function.Consumer;

public class XClass extends XInterface {
    @Getter @Setter private String comment;
    @Getter private final List<XField> fields = new ArrayList<>();
    @Getter private final List<XInterface> interfaces = new ArrayList<>();

    private static final Map<String, XClass> clazzCache = new HashMap<>();
    private static final Map<String, List<Consumer<XClass>>> waitBuildClass = new HashMap<>();

    public XClass(String name, XCoder parent) {
        super(name, parent);
        clazzCache.put(name, this);
        List<Consumer<XClass>> wait = waitBuildClass.get(name);
        if (!Objects.isNull(wait)) {
            wait.forEach(cb -> cb.accept(this));
        }
    }

    @Override
    public XClass addExtend(XInterface parent) {
        if (Objects.isNull(parent)) {
            return this;
        }
        if (!extendes.isEmpty()) {
            throw new IllegalArgumentException("there was a extends class");
        }
        if (!(parent instanceof XClass)) {
            throw new UnsupportedOperationException(String.format("class must extends a class, but %s is a interface", parent.name));
        }
        super.addExtend(parent);
        return this;
    }

    private void addGetter(GenContext context) {
        fields.forEach(f -> context.getLanguage().accept(new AddGetter(this, f)));
    }

    private void addSetter(GenContext context) {
        GenConf conf = context.getConf();
        if (conf.genSetter(this, context.getLanguage())) {
            fields.forEach(f -> {
                if (conf.genSetter(this, f, context.getLanguage())) {
                    context.getLanguage().accept(new AddSetter(this, f));
                }
            });
        }
    }

    private void addToString(GenContext context) {
        fields.forEach(f -> context.getLanguage().accept(new AddToString(this, f)));
    }

    public static String getFullName(String name, XLanguage language) {
        return getClass(name).getFullName(language);
    }

    public static XClass getClass(String name) {
        return getClass(name, null);
    }

    public static XClass getClass(String name, Consumer<XClass> cb) {
        XClass clazz =  clazzCache.get(name);
        if (Objects.isNull(clazz) && !Objects.isNull(cb)) {
            List<Consumer<XClass>> wait = waitBuildClass.get(name);
            if (Objects.isNull(wait)) {
                wait = new ArrayList<>();
                waitBuildClass.put(name, wait);
            }
            wait.add(cb);
        }
        return clazz;
    }

    @Override
    public XClass addImport(String importt) {
        super.addImport(importt);
        return this;
    }

    public XClass implement(XInterface iface) {
        interfaces.add(iface);
        return this;
    }

    public XClass addField(XField field) {
        fields.add(field);
        return this;
    }

    public void checkLoopDepend(Set<String> depends) {
        if (!depends.add(name)) {
            throw new IllegalStateException(String.format("loop depend at bean [%s] in %s", name, depends));
        }
        for (XField f : fields) {
            XType type = f.getType();
            checkExist(type, f.getName());
            if (type.isBase()) {
                continue;
            }
            if (type instanceof XList) {
                type = ((XList) type).getValue();
            } else if (type instanceof XMap) {
                type = ((XMap) type).getValue();
            }

            checkExist(type, f.getName());
            if (type instanceof XBean) {
                String name = ((XBean) type).getName();
                getClass(name).checkLoopDepend(depends);
            }
        }
    }

    private void checkExist(XType type, String fieldName) {
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException(String.format("type not exist of field %s at bean %s ", fieldName, name));
        }
    }

    @Override
    public void print(GenContext context) {
        printPackage(context);
        context.println();
        printImport(context);
        context.println();
        printDefine(context);
        printField(context);
        context.println();
        printConstructor(context);
        context.println();
        printMethod(context);
        context.println("}");
    }

    private void printConstructor(GenContext context) {
        context.getLanguage().accept(new PrintConstructor(this, context));
    }

    protected void printField(GenContext context) {
        fields.forEach(f -> context.vprintln(1, new Field(f)));
    }

    @Override
    protected void printMethod(GenContext context) {
        addGetter(context);
        addSetter(context);
        addToString(context);
        methods.values().forEach(m -> {
            if (context.getConf().genMethod(this, m, context.getLanguage())) {
                context.getLanguage().accept(new PrintMethod(m, context));
                context.println();
            }
        });
    }
}
