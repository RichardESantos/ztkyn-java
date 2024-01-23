package org.gitee.ztkyn.core.ecj;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-05-31 10:36
 * @description ByteCodeJavaFileObject
 * @version 1.0.0
 */
public class ByteCodeJavaFileObject extends SimpleJavaFileObject {

	private static final Logger logger = LoggerFactory.getLogger(ByteCodeJavaFileObject.class);

	private final ByteArrayOutputStream outputStream;

	private final String className;

	/**
	 * Construct a SimpleJavaFileObject of the given kind and with the given class name.
	 * @param className class name
	 * @param kind the kind of this file object
	 */
	protected ByteCodeJavaFileObject(String className, Kind kind) {
		super(URI.create("bytecode://" + className.replaceAll("\\.", "/") + kind.extension), kind);
		this.className = className;
		this.outputStream = new ByteArrayOutputStream(512);
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return this.outputStream;
	}

	/**
	 * FileManager会使用该方法获取编译后的byte，然后将类加载到JVM
	 */
	public byte[] getBytes() {
		return this.outputStream.toByteArray();
	}

	public String getClassName() {
		return className;
	}

}
