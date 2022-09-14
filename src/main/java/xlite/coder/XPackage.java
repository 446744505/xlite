package xlite.coder;

import lombok.Getter;
import xlite.gen.GenContext;
import xlite.gen.Writer;
import xlite.gen.visitor.FileName;
import xlite.gen.visitor.FullPack;
import xlite.language.XLanguage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class XPackage extends AbsCoder implements GenCoder {
    @Getter private final String name;
    private final List<XPackage> children = new ArrayList<>();
    private final List<XClass> classes = new ArrayList<>();

    public XPackage(String name, XCoder parent) {
        super(parent);
        this.name = name;
    }

    public XPackage addChild(XPackage pak) {
        children.add(pak);
        return this;
    }

    public XPackage addClass(XClass clazz) {
        classes.add(clazz);
        return this;
    }

    public static XPackage wrap(String path) {
        String[] names = path.split("\\.");
        XPackage pak = null;
        for (String name : names) {
            pak = new XPackage(name, pak);
        }
        return pak;
    }

    @Override
    public void gen(GenContext context) {
        File dir = mkdir(context);
        for (XClass clazz : classes) {
            try {
                String fileName = context.getLanguage().accept(new FileName(clazz.name));
                File file = new File(dir, fileName);
                file.delete();
                file.createNewFile();
                Writer writer = new Writer(file);
                context.setClassWriter(writer);
                clazz.print(context);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (XPackage pak : children) {
            pak.gen(context);
        }
    }

    @Override
    public void before(GenContext context) {
        if (context.getConf().checkLoopDepend()) {
            checkLoopDepend();
        }
    }

    private void checkLoopDepend() {
        List<XClass> allClass = new ArrayList<>();
        getAllClass(allClass);
        allClass.forEach(clazz -> {
            Set<String> depends = new HashSet<>();
            clazz.checkLoopDepend(depends);
        });
    }

    private void getAllClass(List<XClass> allClass) {
        allClass.addAll(classes);
        children.forEach(pak -> pak.getAllClass(allClass));
    }

    public File mkdir(GenContext context) {
        String path = fullPack(context.getLanguage()).replaceAll("\\.", "/");
        File dir = new File(context.getOutDir(), path);
        dir.mkdirs();
        return dir;
    }

    public String fullPack(XLanguage language) {
        return language.accept(new FullPack(this));
    }
}
