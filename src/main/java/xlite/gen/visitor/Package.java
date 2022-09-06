package xlite.gen.visitor;


import xlite.coder.XPackage;
import xlite.language.Java;

public class Package implements LanguageVisitor<String> {
    private final XPackage pak;

    public Package(XPackage pak) {
        this.pak = pak;
    }

    @Override
    public String visit(Java java) {
        return "package " + pak.fullPack(java) + ";";
    }
}
