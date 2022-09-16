package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XInterface;
import xlite.gen.Writer;
import xlite.language.Java;
import xlite.language.XLanguage;
import xlite.util.Util;

import java.util.List;

public class PrintDefine implements LanguageVisitor<String> {
    private final XInterface iface;

    public PrintDefine(XInterface iface) {
        this.iface = iface;
    }

    @Override
    public String visit(Java java) {
        if (iface instanceof XClass) {
            return jdefineClass(java);
        }
        return jdefineInterface(java);
    }

    private String jdefineClass(XLanguage language) {
        XClass clazz = (XClass) iface;
        Writer writer = new Writer();
        String comment = clazz.getComment();
        if (Util.notEmpty(comment)) {
            writer.println("/**");
            writer.println(comment);
            writer.println("*/");
        }
        writer.print("public class ", clazz.getName());
        if (!clazz.getExtendes().isEmpty()) {
            XInterface parent = clazz.getExtendes().get(0);
            writer.print(" extends ", parent.getFullName(language));
        }

        List<XInterface> interfaces = clazz.getInterfaces();
        if (!interfaces.isEmpty()) {
            writer.print(" implements");
            interfaces.forEach(iface -> writer.print(" ", iface.getFullName(language), ","));
            writer.deleteEnd(1);
        }
        writer.print(" {");
        return writer.getString();
    }

    private String jdefineInterface(XLanguage language) {
        Writer writer = new Writer();
        writer.print("public interface ", iface.getName());
        List<XInterface> extendes = iface.getExtendes();
        if (!extendes.isEmpty()) {
            writer.print(" extends");
            extendes.forEach(iface -> writer.print(" ", iface.getFullName(language), ","));
            writer.deleteEnd(1);
        }
        writer.print(" {");
        return writer.getString();
    }
}
