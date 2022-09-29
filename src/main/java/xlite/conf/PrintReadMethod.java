package xlite.conf;

import xlite.coder.*;
import xlite.excel.XRow;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.language.XLanguage;
import xlite.type.*;
import xlite.type.visitor.BoxName;
import xlite.type.visitor.DefaultValue;
import xlite.type.visitor.TypeVisitor;

public class PrintReadMethod implements LanguageVisitor<XMethod>, TypeVisitor<String> {
    private static final String rowName = "row";
    private static final String countName = "count";
    private static final String methodName = "read";

    private final XClass clazz;
    private final ConfBeanField field;

    public PrintReadMethod(XClass clazz, ConfBeanField field) {
        this.clazz = clazz;
        this.field = field;
    }

    @Override
    public XMethod visit(Java java) {
        clazz.addImport("xlite.excel.XRow")
                .implement(new XInterface("Reader", XPackage.wrap("xlite.excel")));
        Writer body = new Writer();
        boolean isFirstLine = true;
        if (clazz.hasExtend()) {
            isFirstLine = false;
            body.println("super.", methodName, "(", rowName, ", ", countName, ");");
        }
        for (XField field : clazz.getFields()) {
            ConfBeanField confField = (ConfBeanField) field;
            body.print(isFirstLine ? 0 : 2, "");
            if (isFirstLine) {
                isFirstLine = false;
            }
            String line = confField.getType().accept(new PrintReadMethod(clazz, confField), java);
            body.println(line);
        }
        body.deleteEnd(1);//去掉最后一个换行
        XMethod loader = new XMethod(methodName, clazz);
        loader.overrided()
                .addParam(new XField(rowName, new XBean(XRow.class), loader))
                .addParam(new XField(countName, TypeBuilder.INT, loader))
                .addBody(body.getString());
        clazz.addMethod(loader);
        return loader;
    }

    private void assertJava(XLanguage language) {
        if (!(language instanceof Java)) {
            throw new UnsupportedOperationException("PrintLoadMethod only supported java");
        }
    }

    @Override
    public String visit(XLanguage language, XBool t) {
        return visitBase(language, t);
    }

    @Override
    public String visit(XLanguage language, XByte t) {
        return visitBase(language, t);
    }

    @Override
    public String visit(XLanguage language, XInt t) {
        return visitBase(language, t);
    }

    @Override
    public String visit(XLanguage language, XShort t) {
        return visitBase(language, t);
    }

    @Override
    public String visit(XLanguage language, XLong t) {
        return visitBase(language, t);
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return visitBase(language, t);
    }

    @Override
    public String visit(XLanguage language, XDouble t) {
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
        if (vt instanceof TypeBase) {
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
    public String visit(XLanguage language, XEnum t) {
        assertJava(language);
        t.assertInner();
        String enumBoxName = XClass.getFullName(t.name(), language);
        String enumFieldName = String.format("%s.getCell(\"%s\").asString()", rowName, field.getFromCol());
        return String.format("this.%s = %s.%s(%s);", field.getName(), enumBoxName, PrintEnumValueMethod.ENUM_METHOD_VALUE, enumFieldName);
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        throw new UnsupportedOperationException("PrintLoadMethod unsupported type void");
    }

    @Override
    public String visit(XLanguage language, XAny t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(XLanguage language, XRange t) {
        assertJava(language);
        return String.format("this.%s = %s.getCell(\"%s\", %s).asRange(\"%s\");", field.getName(), rowName, field.getFromCol(), countName, t.getValue().name());
    }

    @Override
    public String visit(XLanguage language, XTime t) {
        assertJava(language);
        return String.format("this.%s = xlite.util.Util.strToTime(%s.getCell(\"%s\", %s).asString());", field.getName(), rowName, field.getFromCol(), countName);
    }

    @Override
    public String visit(XLanguage language, XDate t) {
        assertJava(language);
        return String.format("this.%s = new xlite.type.inner.DateTime(%s.getCell(\"%s\", %s).asString());", field.getName(), rowName, field.getFromCol(), countName);
    }
}
