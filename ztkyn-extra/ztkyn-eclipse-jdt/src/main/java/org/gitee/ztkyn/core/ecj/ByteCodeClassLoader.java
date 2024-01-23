package org.gitee.ztkyn.core.ecj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-05-31 10:38
 * @description ByteCodeClassLoader
 * @version 1.0.0
 */
public class ByteCodeClassLoader extends ClassLoader {

	private static final Logger logger = LoggerFactory.getLogger(ByteCodeClassLoader.class);

	private ByteCodeJavaFileObject byteCodeJavaFileObject;

	public ByteCodeClassLoader(ClassLoader parent) {
		super(parent);
		// this is to make the stack depth consistent with 1.1
		// JDK 17 已经移除
		// SecurityManager security = System.getSecurityManager();
		// if (security != null) {
		// security.checkCreateClassLoader();
		// }
	}

	public void setByteCodeJavaFileObject(ByteCodeJavaFileObject byteCodeJavaFileObject) {
		this.byteCodeJavaFileObject = byteCodeJavaFileObject;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (byteCodeJavaFileObject.getClassName().equals(name)) {
			byte[] bytecode = byteCodeJavaFileObject.getBytes();
			return defineClass(name, bytecode, 0, bytecode.length);
		}
		throw new ClassNotFoundException(name);
	}

}
