package xlite.compile;

import org.apache.commons.io.FilenameUtils;
import xlite.util.Util;

import javax.tools.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
    private static DiagnosticCollector<JavaFileObject> DIAGNOSTIC_COLLECTOR = new DiagnosticCollector<>();
    private final File srcDir;

    public Compiler(File srcDir) {
        this.srcDir = srcDir;
    }

    public ClassLoader compile() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = new ArrayList<>();
        options.add("-source");
        options.add("1.8");
        options.add("-target");
        options.add("1.8");
        StandardJavaFileManager manager = compiler.getStandardFileManager(DIAGNOSTIC_COLLECTOR, null, null);
        XClassLoader classLoader = new XClassLoader(Thread.currentThread().getContextClassLoader());
        XJavaFileManager fileManager = new XJavaFileManager(manager, classLoader);
        Iterable<? extends JavaFileObject> fileObjects = manager.getJavaFileObjects(Util.javaFiles(srcDir).toArray(new File[]{}));
        fileObjects.forEach(fileObject -> {
            String clazzName = FilenameUtils.getBaseName(fileObject.getName());
            fileManager.addJavaFileObject(StandardLocation.SOURCE_PATH, "", clazzName, fileObject);
        });
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, DIAGNOSTIC_COLLECTOR, options, null, fileObjects);
        task.call();
        return classLoader;
    }

}
