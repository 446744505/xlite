package xlite.conf;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.PrintCheckMethod;
import xlite.language.Java;
import xlite.util.Util;

import java.util.Objects;

public class ConfPrintCheckMethod extends PrintCheckMethod {
    public ConfPrintCheckMethod(XClass clazz) {
        super(clazz);
    }

    @Override
    public XMethod visit(Java java) {
        XClass topParent = clazz.getTopParent();
        if (Objects.isNull(topParent)) {
            topParent = clazz;
        }
        XMethod method = super.visit(java);
        boolean isFirstLine = Util.isEmpty(method.getBody());
        Writer body = new Writer();
        if (!isFirstLine) {
            body.println();
        }
        for (XField field : clazz.getFields()) {
            int tab = 2;
            if (isFirstLine) {
                tab = 0;
                isFirstLine = false;
            }
            ConfBeanField confBeanField = (ConfBeanField) field;
            String uniq = confBeanField.getUniqCheck();
            if (Util.notEmpty(uniq) && "true".equals(uniq.toLowerCase())) {
                body.println(tab, String.format("xlite.conf.UniqCheck.check(%s.class, \"%s\", %s);",
                        topParent.getFullName(java), field.getName(), field.getName()));
            }

            String foreign = confBeanField.getForeignCheck();
            if (Util.notEmpty(foreign)) {
                boolean checkChild = foreign.startsWith("*");
                String[] arr = foreign.replace("*", "").split("\\.");
                String checker = arr[0];
                checker = XClass.getFullName(checker, java);
                String checkField = "id";
                if (arr.length > 1) {
                    checkField = arr[1];
                }
                body.println(tab, String.format("xlite.conf.ForeignCheck.addForeignCheck(this, \"%s\", %s.class, \"%s\", %s, %s);",
                        field.getName(), checker, checkField, field.getName(), checkChild));
            }

            String must = confBeanField.getMustCheck();
            if (Util.notEmpty(must)) {
                String[] arr = must.split("\\.");
                String checker = arr[0];
                checker = XClass.getFullName(checker, java);
                String checkField = "id";
                if (arr.length > 1) {
                    checkField = arr[1];
                }
                body.println(tab, String.format("xlite.conf.MustCheck.addMustCheck(%s.class, \"%s\", %s.class, \"%s\");",
                        clazz.getFullName(java), field.getName(), checker, checkField));
            }
        }
        body.deleteEnd(1);

        method.addBody(body.getString());
        return method;
    }
}
