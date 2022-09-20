package xlite.coder;

import xlite.gen.GenContext;
import xlite.gen.visitor.PrintConst;

public class XEnumer extends XClass {
    public XEnumer(String name, XCoder parent) {
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
        fields.stream().map(f -> (XEnumField)f).forEach(f -> context.println(1, new PrintConst(f)));
    }
}
