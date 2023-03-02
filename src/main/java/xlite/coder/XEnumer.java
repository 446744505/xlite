package xlite.coder;

import xlite.gen.GenContext;
import xlite.gen.visitor.PrintEnumField;
import xlite.type.XType;

public class XEnumer extends XClass {
    private XType inner;

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
        context.println();
        printMethod(context);
        context.println("}");
    }

    @Override
    protected void printField(GenContext context) {
        fields.stream().map(f -> (XEnumField)f).forEach(f -> context.println(1, new PrintEnumField(f)));
    }

    public XType getInner() {
        return inner;
    }

    public void setInner(XType inner) {
        this.inner = inner;
    }
}
