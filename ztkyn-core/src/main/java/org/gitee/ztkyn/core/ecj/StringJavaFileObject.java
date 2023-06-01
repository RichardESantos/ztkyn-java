package org.gitee.ztkyn.core.ecj;
import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @date 2023-05-31 10:37
 * @description StringJavaFileObject
 * @version 1.0.0
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
    private static final Logger logger = LoggerFactory.getLogger(StringJavaFileObject.class);
    private CharSequence content;
    /**
     * Construct a SimpleJavaFileObject of the given className and with the
     * given content.
     *
     * @param className class name
     * @param content java source string
     */
    protected StringJavaFileObject(String className, String content) {
        super(URI.create("bytecode://" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension),  Kind.SOURCE);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return content;
    }

}
