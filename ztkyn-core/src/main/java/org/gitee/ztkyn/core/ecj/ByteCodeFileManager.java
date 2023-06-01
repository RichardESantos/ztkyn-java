package org.gitee.ztkyn.core.ecj;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @date 2023-05-31 10:35
 * @description ByteCodeFileManager
 * @version 1.0.0
 */
public class ByteCodeFileManager extends ForwardingJavaFileManager {
    private static final Logger logger = LoggerFactory.getLogger(ByteCodeFileManager.class);
    private ByteCodeJavaFileObject byteCodeJavaFileObject;
    private ByteCodeClassLoader byteCodeClassLoader;
    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    protected ByteCodeFileManager(JavaFileManager fileManager) {
        super(fileManager);
        byteCodeClassLoader = new ByteCodeClassLoader(getClass().getClassLoader());
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        this.byteCodeJavaFileObject = new ByteCodeJavaFileObject(className, kind);
        this.byteCodeClassLoader.setByteCodeJavaFileObject(byteCodeJavaFileObject);
        return this.byteCodeJavaFileObject;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return byteCodeClassLoader;
    }

    public byte[] getByteCode() {
        return byteCodeJavaFileObject.getBytes();
    }
}
