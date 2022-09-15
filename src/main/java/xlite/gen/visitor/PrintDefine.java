package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XInterface;
import xlite.gen.Writer;
import xlite.language.Java;
import xlite.language.XLanguage;

import java.util.List;
import java.util.Objects;

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
        if (!Objects.isNull(comment) && !comment.isEmpty()) {
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
            StringBuilder sb = new StringBuilder();
            interfaces.forEach(iface -> sb.append(" ").append(iface.getFullName(language)).append(","));
            sb.delete(sb.length() - 1, sb.length());
            writer.print(sb.toString());
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
            StringBuilder sb = new StringBuilder();
            extendes.forEach(iface -> sb.append(" ").append(iface.getFullName(language)).append(","));
            sb.delete(sb.length() - 1, sb.length());
            writer.print(sb.toString());
        }
        writer.print(" {");
        return writer.getString();
    }
}
