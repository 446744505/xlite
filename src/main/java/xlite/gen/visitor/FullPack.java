package xlite.gen.visitor;

import xlite.coder.XPackage;
import xlite.language.Java;

import java.util.Objects;

public class FullPack implements LanguageVisitor<String> {
    private final XPackage pak;

    public FullPack(XPackage pak) {
        this.pak = pak;
    }

    @Override
    public String visit(Java java) {
        String path = pak.getName();
        XPackage p = (XPackage) pak.getParent();
        while (Objects.nonNull(p)) {
            path = p.getName() + "." + path;
            p = (XPackage) p.getParent();
        }
        return path;
    }
}
