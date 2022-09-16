package xlite.coder;

import xlite.gen.GenContext;
import xlite.gen.visitor.Enumer;

public class XEnum extends XClass {
    public XEnum(String name, XCoder parent) {
        super(name, parent);
    }

    public void print(GenContext context) {
        printPackage(context);
        context.println();
        printImport(context);
        context.println();
        printDefine(context);
        printField(context);
        context.println("}");
    }

    @Override
    protected void printField(GenContext context) {
        fields.stream().map(f -> (XEnumField)f).forEach(f -> context.println(1, new Enumer(f)));
    }
}
