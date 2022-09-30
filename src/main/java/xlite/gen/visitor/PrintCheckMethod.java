package xlite.gen.visitor;

import xlite.coder.*;
import xlite.gen.Writer;
import xlite.language.Java;
import xlite.type.TypeBase;
import xlite.type.XEnum;
import xlite.type.XType;
import xlite.type.visitor.RangeCheck;
import xlite.util.Util;

public class PrintCheckMethod implements LanguageVisitor<XMethod> {
    private static final String methodName = "check";
    protected final XClass clazz;

    public PrintCheckMethod(XClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        clazz.implement(new XInterface("Checker", XPackage.wrap("xlite")));
        Writer body = new Writer();
        boolean isFirstLine = true;
        if (clazz.hasExtend()) {
            isFirstLine = false;
            body.println("super.check();");
        }
        for (XField field : clazz.getFields()) {
            String range = field.getRangeCheck();
            XType checkType = field.getType();
            if (Util.isEmpty(range) &&
                    (checkType instanceof TypeBase || checkType instanceof XEnum)) {//没必要生成check代码(其实生成也没关系)
                continue;
            }
            String line = checkType.accept(new RangeCheck(field.getName(), range), java);
            if (Util.notEmpty(line)) {
                int tab = 2;
                if (isFirstLine) {
                    tab = 0;
                    isFirstLine = false;
                }
                body.println(tab, line + ";");
            }
        }
        body.deleteEnd(1);
        XMethod method = new XMethod(methodName, clazz);
        method.overrided().throwed("xlite.CheckException")
                .addBody(body.getString());
        clazz.addMethod(method);
        return method;
    }
}
