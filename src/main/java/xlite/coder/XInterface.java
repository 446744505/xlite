package xlite.coder;

import lombok.Getter;
import xlite.gen.GenContext;
import xlite.gen.visitor.Define;
import xlite.gen.visitor.Import;
import xlite.language.XLanguage;

import java.util.ArrayList;
import java.util.List;

public class XInterface extends AbsCoder {
    @Getter protected final String name;
    @Getter protected List<XInterface> extendes = new ArrayList<>();
    protected final List<String> imports = new ArrayList<>();
    protected final List<XMethod> methods = new ArrayList<>();

    public XInterface(String name, XCoder parent) {
        super(parent);
        this.name = name;
    }

    public XInterface addExtend(XInterface parent) {
        extendes.add(parent);
        return this;
    }

    public XInterface addImport(String importt) {
        imports.add(importt);
        return this;
    }

    public XInterface addMethod(XMethod method) {
        methods.add(method);
        return this;
    }

    public String getFullName(XLanguage language) {
        XPackage pak = (XPackage) parent;
        return pak.fullPack(language) + "." + name;
    }

    public void print(GenContext context) {
        printPackage(context);
        context.println();
        printImport(context);
        context.println();
        printDefine(context);
        context.println();
        printMethod(context);
    }

    protected void printPackage(GenContext context) {
        XPackage pak = (XPackage) parent;
        context.vprintln(new xlite.gen.visitor.Package(pak));
    }

    protected void printImport(GenContext context) {
        imports.forEach(imp -> context.vprintln(new Import(imp)));
    }

    protected void printDefine(GenContext context) {
        context.vprintln(new Define(this));
    }

    protected void printMethod(GenContext context) {

    }
}
