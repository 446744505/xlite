package xlite.coder;

import xlite.gen.GenConf;
import xlite.gen.GenContext;
import xlite.gen.visitor.*;
import xlite.language.XLanguage;
import xlite.type.HaveValue;
import xlite.type.TypeBase;
import xlite.type.XBean;
import xlite.type.XType;

import java.util.*;
import java.util.function.Consumer;

public class XClass extends XInterface {
    private String comment;
    protected final List<XField> fields = new ArrayList<>();
    private final List<XInterface> interfaces = new ArrayList<>();

    private static final Map<String, XClass> clazzCache = new HashMap<>();
    private static final Map<String, List<Consumer<XClass>>> waitBuildClass = new HashMap<>();

    public XClass(String name, XCoder parent) {
        super(name, parent);
        if (Objects.nonNull(clazzCache.put(name, this))) {
            throw new IllegalStateException(String.format("type of repetition:", name));
        }
        List<Consumer<XClass>> wait = waitBuildClass.get(name);
        if (Objects.nonNull(wait)) {
            wait.forEach(cb -> cb.accept(this));
        }
    }

    public static void register(Class clazz) {
        String[] paks = clazz.getName().split("\\.");
        StringBuilder pakPath = new StringBuilder();
        for (int i = 0; i < paks.length - 1; i++) {
            pakPath.append(paks[i]);
        }
        XPackage pak = new XPackage(pakPath.toString(), null);
        new XClass(clazz.getSimpleName(), pak);
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

    public XClass getTopParent() {
        if (extendes.isEmpty()) {
            return null;
        }
        XClass parent = (XClass) extendes.get(0);
        if (Objects.nonNull(parent)) {
            XClass pp = parent.getTopParent();
            if (Objects.nonNull(pp)) {
                return pp;
            }
        }
        return parent;
    }

    private void addGetter(GenContext context) {
        GenConf conf = context.getConf();
        if (conf.genGetter(this, context.getLanguage())) {
            fields.forEach(f -> {
                if (conf.genGetter(this, f, context.getLanguage())) {
                    context.getLanguage().accept(new AddGetter(this, f));
                }
            });
        }
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
        context.getLanguage().accept(new AddToString(this));
    }

    private void addEquals(GenContext context) {
        context.getLanguage().accept(new AddEquals(this));
    }

    private void addHashCode(GenContext context) {
        context.getLanguage().accept(new AddHashCode(this));
    }

    public static String getFullName(String name, XLanguage language) {
        return getClass(name).getFullName(language);
    }

    public static XClass getClass(String name) {
        return getClass(name, null);
    }

    public static XClass getClass(String name, Consumer<XClass> cb) {
        XClass clazz =  clazzCache.get(name);
        if (Objects.isNull(clazz) && Objects.nonNull(cb)) {
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
        if (Objects.nonNull(iface)) {
            interfaces.add(iface);
        }
        return this;
    }

    public XClass addField(XField field) {
        if (Objects.nonNull(field)) {
            fields.add(field);
        }
        return this;
    }

    @Override
    public XClass addContent(String body) {
        super.addContent(body);
        return this;
    }

    public void checkLoopDepend(Set<String> depends) {
        if (!depends.add(name)) {
            throw new IllegalStateException(String.format("loop depend at bean [%s] in %s", name, depends));
        }
        for (XField f : fields) {
            XType type = f.getType();
            checkExist(type, f.getName());
            if (type instanceof TypeBase) {
                continue;
            }
            if (type instanceof HaveValue) {
                type = ((HaveValue) type).getValue();
            }

            checkExist(type, f.getName());
            if (type instanceof XBean) {
                String name = type.name();
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
        printContents(context);
        context.println("}");
    }

    private void printConstructor(GenContext context) {
        context.getLanguage().accept(new PrintConstructor(this, context));
    }

    protected void printField(GenContext context) {
        fields.forEach(f -> context.println(1, new PrintField(f)));
    }

    @Override
    protected void printMethod(GenContext context) {
        addGetter(context);
        addSetter(context);
        addToString(context);
        addEquals(context);
        addHashCode(context);
        methods.values().forEach(m -> {
            if (context.getConf().genMethod(this, m, context.getLanguage())) {
                context.getLanguage().accept(new PrintMethod(m, context));
                context.println();
            }
        });
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<XField> getFields() {
        return fields;
    }

    public List<XInterface> getInterfaces() {
        return interfaces;
    }
}
