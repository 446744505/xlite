package xlite.coder;

import lombok.Getter;
import xlite.gen.GenContext;
import xlite.gen.visitor.Import;
import xlite.gen.visitor.PrintDefine;
import xlite.language.XLanguage;
import xlite.util.Util;

import java.util.*;

public class XInterface extends AbsCoder {
    @Getter protected final String name;
    @Getter protected List<XInterface> extendes = new ArrayList<>();
    protected final List<String> imports = new ArrayList<>();
    protected final Map<String, XMethod> methods = new LinkedHashMap<>();
    @Getter private final List<String> contents = new ArrayList<>();

    public XInterface(String name, XCoder parent) {
        super(parent);
        this.name = name;
    }

    public XInterface addExtend(XInterface parent) {
        if (Objects.nonNull(parent)) {
            extendes.add(parent);
        }
        return this;
    }

    public XInterface addImport(String importt) {
        if (Objects.nonNull(importt)) {
            imports.add(importt);
        }
        return this;
    }

    public XInterface addMethod(XMethod method) {
        if (Objects.nonNull(method)) {
            methods.put(method.getName(), method);
        }
        return this;
    }

    public XInterface addContent(String body) {
        if (Util.notEmpty(body)) {
            contents.add(body);
        }
        return this;
    }

    public String getFullName(XLanguage language) {
        XPackage pak = (XPackage) parent;
        return pak.fullPack(language) + "." + name;
    }

    public boolean hasExtend() {
        return !extendes.isEmpty();
    }

    public void print(GenContext context) {
        printPackage(context);
        context.println();
        printImport(context);
        context.println();
        printDefine(context);
        context.println();
        printMethod(context);
        printContents(context);
        context.println("}");
    }

    protected void printPackage(GenContext context) {
        XPackage pak = (XPackage) parent;
        context.println(new xlite.gen.visitor.Package(pak));
    }

    protected void printImport(GenContext context) {
        imports.forEach(imp -> context.println(new Import(imp)));
    }

    protected void printDefine(GenContext context) {
        context.println(new PrintDefine(this));
    }

    protected void printMethod(GenContext context) {

    }

    protected void printContents(GenContext context) {
        contents.forEach(body -> context.println(body));
    }
}
