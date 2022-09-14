package xlite.conf;

import xlite.coder.*;
import xlite.excel.XRow;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.language.XLanguage;
import xlite.type.*;
import xlite.type.visitor.BoxName;
import xlite.type.visitor.DefaultValue;
import xlite.type.visitor.TypeVisitor;

public class PrintLoadMethod implements LanguageVisitor<XMethod>, TypeVisitor<String> {
    private static final String rowName = "row";
    private static final String countName = "count";
    private static final String methodName = "load";

    private final XClass clazz;
    private final ConfField field;

    public PrintLoadMethod(XClass clazz, ConfField field) {
        this.clazz = clazz;
        this.field = field;
    }

    public void make() {
        Java.INSTANCE.accept(this);
    }

    @Override
    public XMethod visit(Java java) {
        clazz.addImport("xlite.excel.XRow")
                .implement(new XInterface("Loader", XPackage.wrap("xlite.excel")));
        StringBuilder body = new StringBuilder(50);
        boolean isFirstLine = true;
        if (clazz.hasExtend()) {
            isFirstLine = false;
            body.append("super.").append(methodName).append("(").append(rowName).append(", ").append(countName).append(");\n");
        }
        for (XField field : clazz.getFields()) {
            ConfField confField = (ConfField) field;
            body.append(isFirstLine ? "" : "        ");
            if (isFirstLine) {
                isFirstLine = false;
            }
            String line = confField.getType().accept(new PrintLoadMethod(clazz, confField), java);
            body.append(line).append("\n");
        }
        body.delete(body.length() - 1, body.length());//去掉最后一个换行
        XMethod loader = new XMethod(methodName, clazz);
        loader.addParam(new XField(rowName, new XBean(XRow.class), loader))
                .addParam(new XField(countName, TypeBuilder.INT, loader))
                .returned(XVoid.INSTANCE)
                .addBody(body.toString());
        clazz.addMethod(loader);
        return loader;
    }

    private void assertJava(XLanguage language) {
        if (!(language instanceof Java)) {
            throw new UnsupportedOperationException("PrintLoadMethod only supported java");
        }
    }

    @Override
    public String visit(XLanguage language, XInt t) {
        return visitBase(language, t);
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return visitBase(language, t);
    }

    @Override
    public String visit(XLanguage language, XString t) {
        return visitBase(language, t);
    }

    private String visitBase(XLanguage language, TypeBase t) {
        assertJava(language);
        String boxName = t.accept(BoxName.INSTANCE, language);
        return String.format("this.%s = %s.getCell(\"%s\", %s).as%s();", field.getName(), rowName, field.getFromCol(), countName, boxName);
    }

    @Override
    public String visit(XLanguage language, XList t) {
        assertJava(language);
        XType vt = t.getValue();
        if (vt.isBase()) {
            String boxName = vt.accept(BoxName.INSTANCE, language);
            return String.format("xlite.excel.VList.read(\"%s\", %s, cell -> this.%s.add(cell.as%s()));", field.getFromCol(), rowName, field.getName(), boxName);
        }
        String defaultValue = vt.accept(DefaultValue.INSTANCE, language);
        return String.format("xlite.excel.VList.read(%s, () -> %s, obj -> this.%s.add(obj));", rowName, defaultValue, field.getName());
    }

    @Override
    public String visit(XLanguage language, XMap t) {
        throw new UnsupportedOperationException("conf unsupported type map");
    }

    @Override
    public String visit(XLanguage language, XBean t) {
        assertJava(language);
        return String.format("this.%s.%s(%s, %s);", field.getName(), methodName, rowName, countName);
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        throw new UnsupportedOperationException("PrintLoadMethod unsupported type void");
    }
}
