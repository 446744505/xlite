package xlite.compile;

import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class XJavaFileObject extends SimpleJavaFileObject {

    public static final String CLASS_EXTENSION = ".class";

    public static final String JAVA_EXTENSION = ".java";

    private static URI fromClassName(String className) {
        try {
            return new URI(className);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(className, e);
        }
    }

    private ByteArrayOutputStream byteCode;
    private final CharSequence sourceCode;

    public XJavaFileObject(String className, CharSequence sourceCode) {
        super(fromClassName(className + JAVA_EXTENSION), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    public XJavaFileObject(String fullClassName, Kind kind) {
        super(fromClassName(fullClassName), kind);
        this.sourceCode = null;
    }

    public XJavaFileObject(URI uri, Kind kind) {
        super(uri, kind);
        this.sourceCode = null;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return sourceCode;
    }

    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(getByteCode());
    }

    // 注意这个方法是编译结果回调的OutputStream，回调成功后就能通过下面的getByteCode()方法获取目标类编译后的字节码字节数组
    @Override
    public OutputStream openOutputStream() {
        return byteCode = new ByteArrayOutputStream();
    }

    public byte[] getByteCode() {
        return byteCode.toByteArray();
    }
}
