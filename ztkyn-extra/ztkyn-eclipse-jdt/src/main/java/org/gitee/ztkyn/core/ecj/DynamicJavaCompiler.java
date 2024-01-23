package org.gitee.ztkyn.core.ecj;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

import javax.tools.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-05-31 10:32
 * @description DynamicJavaCompiler
 * @version 1.0.0
 */
public class DynamicJavaCompiler implements Closeable {

	private static final Logger logger = LoggerFactory.getLogger(DynamicJavaCompiler.class);

	// sum java compiler
	public static final String JAVAC = "JAVAC";

	// eclipse java compiler, not support JSR 269 API
	public static final String ECJ = "ECJ";

	private ByteCodeFileManager fileManager;

	private JavaCompiler compiler;

	private DiagnosticCollector<JavaFileObject> diagnostics;

	public DynamicJavaCompiler() {
		this(JAVAC);
	}

	public DynamicJavaCompiler(String model) {
		initFileManager(model);
	}

	private void initFileManager(String model) {
		if (fileManager == null) {
			if (JAVAC.equals(model)) {
				compiler = ToolProvider.getSystemJavaCompiler();
				diagnostics = new DiagnosticCollector<>();
				fileManager = new ByteCodeFileManager(compiler.getStandardFileManager(diagnostics, null, null));
			}
			else {
				compiler = new EclipseCompiler();
				diagnostics = new DiagnosticCollector<>();
				fileManager = new ByteCodeFileManager(compiler.getStandardFileManager(diagnostics, null, null));
			}
		}
	}

	public boolean javaCompiler(String className, String sourceCode) {
		List<JavaFileObject> compilationUnits = new ArrayList<>();
		compilationUnits.add(new StringJavaFileObject(className, sourceCode));
		JavaCompiler.CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnostics, null, null,
				compilationUnits);

		boolean result = compilationTask.call();
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
			logger.error("Error on line {} in {}", diagnostic.getLineNumber(), diagnostic.getMessage(null));
		}
		return result;
	}

	public Class loadClass(String className) throws ClassNotFoundException {
		return fileManager.getClassLoader(null).loadClass(className);
	}

	public byte[] getByteCode() {
		return fileManager.getByteCode();
	}

	@Override
	public void close() throws IOException {
		this.fileManager.close();
	}

}
