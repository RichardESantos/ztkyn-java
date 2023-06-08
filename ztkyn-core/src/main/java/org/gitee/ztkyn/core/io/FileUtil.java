package org.gitee.ztkyn.core.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;

import org.gitee.ztkyn.core.exception.FileOperationException;
import org.gitee.ztkyn.core.function.SomeObj;
import org.gitee.ztkyn.core.function.PredicateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.gitee.ztkyn.core.string.CharsetUtil.UTF8;

/**
 * @author richard
 * @version 1.0
 * @description 文件工具类
 * @date 2023/1/16 13:15
 */
public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 文本重命名
	 * @param dirPath 文件所在文件夹路径
	 * @param oldName 文件名称
	 * @param newName 新的名称
	 */
	public static boolean renameFile(String dirPath, String oldName, String newName) {
		if (Objects.equals(oldName, newName)) {
			logger.warn("文件名称一致，没有发生变化");
			return true;
		}
		Path oldFilePath = Paths.get(dirPath, oldName);
		File oldFile = oldFilePath.toFile();
		checkFileAccessException(oldFile);
		return oldFile.renameTo(Paths.get(dirPath, newName).toFile());
	}

	/**
	 * 移动文件
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean moveFile(String oldPath, String newPath) {
		if (Objects.equals(oldPath, newPath)) {
			logger.warn("文件名称一致，没有发生变化");
			return true;
		}
		Path oldFilePath = Paths.get(oldPath);
		File oldFile = oldFilePath.toFile();
		checkFileAccessException(oldFile);
		return oldFile.renameTo(Paths.get(newPath).toFile());
	}

	/**
	 * 移动文件夹
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean moveFolder(String oldPath, String newPath, boolean isReplace) {
		if (Objects.equals(oldPath, newPath)) {
			logger.warn("文件名称一致，没有发生变化");
			return true;
		}
		File oldFolder = Paths.get(oldPath).toFile();
		checkFolderAccessException(oldFolder);
		try {
			checkDestPathAccess(newPath, isReplace);
			SomeObj.of(oldFolder.listFiles(), PredicateUtil.notNull()).ifTrue(files -> {
				for (File file : files) {
					file.renameTo(Paths.get(newPath, file.getName()).toFile());
				}
			}).ifFalse(() -> {
				logger.warn(MessageFormat.format("源文件夹【{0}】为空", oldPath));
			});
		}
		catch (IOException e) {
			throw new FileOperationException(e);
		}
		return true;
	}

	private static void checkDestPathAccess(String newPath, boolean isReplace) throws IOException {
		File newFolder = Paths.get(newPath).toFile();
		if (newFolder.exists()) {
			if (newFolder.isDirectory()) {
				if (!newFolder.canWrite()) {
					throw new FileOperationException(MessageFormat.format("目标文件夹【{0}】为不允许写入", newPath));
				}
				if (isReplace) {
					newFolder.delete();
					newFolder.createNewFile();
				}
			}
			else if (newFolder.isFile()) {
				throw new FileOperationException(MessageFormat.format("目标文件夹【{0}】为文件", newPath));
			}
		}
		else {
			newFolder.createNewFile();
		}
	}

	/**
	 * 以 UTF-8 编码格式读取文本
	 * @param file
	 * @return
	 */
	public static String readUTF8Content(File file) {
		return readContent(file, UTF8);
	}

	/**
	 * 以 UTF-8 编码格式读取文本
	 * @param path
	 * @return
	 */
	public static String readUTF8Content(String path) {
		return readContent(Paths.get(path).toFile(), UTF8);
	}

	/**
	 * 以 UTF-8 编码格式读取文本
	 * @param file
	 * @return
	 */
	public static List<String> readUTF8Lines(File file) {
		return readAllLines(file, UTF8);
	}

	/**
	 * 以 UTF-8 编码格式读取文本
	 * @param path
	 * @return
	 */
	public static List<String> readUTF8Lines(String path) {
		return readAllLines(path, UTF8);
	}

	/**
	 * 以 指定 编码格式读取文本
	 * @param path
	 * @param charset
	 * @return
	 */
	public static List<String> readAllLines(String path, Charset charset) {
		return readAllLines(Paths.get(path).toFile(), charset);
	}

	/**
	 * 以 指定 编码格式读取文本
	 * @param file
	 * @param consumer
	 */
	public static void readUTF8Lines(File file, Consumer<String> consumer) {
		readLines(file, UTF8, consumer);
	}

	/**
	 * 以 UTF-8 编码格式输出文本
	 * @param filePath
	 * @param lines
	 */
	public static void writeUTF8Lines(String filePath, List<String> lines) {
		writeUTF8Lines(filePath, lines, false);
	}

	/**
	 * 以 UTF-8 编码格式输出文本
	 * @param filePath
	 * @param lines
	 * @param append
	 */
	public static void writeUTF8Lines(String filePath, List<String> lines, boolean append) {
		writeLines(filePath, lines, UTF8, append);
	}

	/**
	 * 以 指定 编码格式输出文本
	 * @param filePath
	 * @param lines
	 * @param charset
	 * @param append
	 */
	public static void writeLines(String filePath, List<String> lines, Charset charset, boolean append) {
		writeLines(Paths.get(filePath), lines, charset, append);
	}

	/**
	 * 以 指定 编码格式输出文本
	 * @param file
	 * @param lines
	 * @param charset
	 * @param append
	 */
	public static void writeLines(File file, List<String> lines, Charset charset, boolean append) {
		writeLines(file.toPath(), lines, charset, append);
	}

	/**
	 * 以 UTF-8 编码格式输出文本
	 * @param filePath
	 * @param content
	 */
	public static void writeUTF8Content(String filePath, String content) {
		writeUTF8Content(filePath, content, false);
	}

	/**
	 * 以 UTF-8 编码格式输出文本
	 * @param filePath
	 * @param content
	 * @param append
	 */
	public static void writeUTF8Content(String filePath, String content, boolean append) {
		writeContent(filePath, content, UTF8, append);
	}

	/**
	 * 以 指定 编码格式输出文本
	 * @param filePath
	 * @param content
	 * @param charset
	 * @param append
	 */
	public static void writeContent(String filePath, String content, Charset charset, boolean append) {
		writeContent(Paths.get(filePath), content, charset, append);
	}

	/**
	 * 以 指定 编码格式输出文本
	 * @param file
	 * @param content
	 * @param charset
	 * @param append
	 */
	public static void writeContent(File file, String content, Charset charset, boolean append) {
		writeContent(file.toPath(), content, charset, append);
	}

	/**
	 * 检车文件是否能正常读取
	 * @param file
	 * @return
	 */
	public static boolean checkFileAccessError(File file) {
		try {
			checkFileAccessException(file);
		}
		catch (FileOperationException exception) {
			logger.warn(exception.getMessage());
			return true;
		}
		return false;
	}

	/**
	 * 检车文件是否能正常读取
	 * @param file
	 * @return
	 */
	public static void checkFileAccessException(File file) {
		String path = file.getAbsolutePath();
		if (!file.exists()) {
			throw new FileOperationException(MessageFormat.format("文件读取失败，【{0}】文件不存在", path));
		}
		if (!file.isFile()) {
			throw new FileOperationException(MessageFormat.format("文件读取失败，【{0}】不是文件", path));
		}
		if (!file.canRead()) {
			throw new FileOperationException(MessageFormat.format("文件读取失败，【{0}】权限不足", path));
		}
	}

	/**
	 * 检车文件夹是否能正常读取
	 * @param file
	 * @return
	 */
	public static boolean checkFolderAccessError(File file) {
		try {
			checkFolderAccessException(file);
		}
		catch (FileOperationException exception) {
			logger.warn(exception.getMessage());
			return true;
		}
		return false;
	}

	/**
	 * 检车文件夹是否能正常读取
	 * @param file
	 * @return
	 */
	public static void checkFolderAccessException(File file) {
		String path = file.getAbsolutePath();
		if (!file.exists()) {
			throw new FileOperationException(MessageFormat.format("文件读取失败，【{0}】文件夹不存在", path));
		}
		if (!file.isDirectory()) {
			throw new FileOperationException(MessageFormat.format("文件读取失败，【{0}】不是文件夹", path));
		}
		if (!file.canRead()) {
			throw new FileOperationException(MessageFormat.format("文件读取失败，【{0}】权限不足", path));
		}
	}

	/**
	 * 以指定的编码格式读取文本内容，编码为文件本身的编码格式，
	 * 不论源码文件是什么格式，同样的字符串，最后得到的unicode字节数组是完全一致的，显示的时候，也是转成GBK来显示（跟OS环境有关）
	 * @param file
	 * @param charset
	 * @return
	 */
	public static List<String> readAllLines(File file, Charset charset) {
		if (checkFileAccessError(file))
			return new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, charset))) {
			List<String> lineList = new ArrayList<>();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				lineList.add(line);
			}
			return lineList;
		}
		catch (IOException e) {
			logger.error("文件读取异常", e);
			return new ArrayList<>();
		}
	}

	/**
	 * 以指定的编码格式读取文本内容，编码为文件本身的编码格式，
	 * @param path
	 * @param charset
	 * @return
	 */
	public static List<String> readAllLines(Path path, Charset charset) {
		if (checkFileAccessError(path.toFile()))
			return new ArrayList<>();
		try (BufferedReader bufferedReader = Files.newBufferedReader(path, charset)) {
			List<String> lineList = new ArrayList<>();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				lineList.add(line);
			}
			return lineList;
		}
		catch (IOException e) {
			logger.error("文件读取异常", e);
			return new ArrayList<>();
		}
	}

	/**
	 * 以指定的编码格式读取文本内容，采用边读取边消费的模式，减少内存占用 以指定的编码格式读取文本内容，编码为文件本身的编码格式，，
	 * 不论源码文件是什么格式，同样的字符串，最后得到的unicode字节数组是完全一致的，显示的时候，也是转成GBK来显示（跟OS环境有关）
	 * @param file
	 * @param charset
	 * @param consumer
	 */
	public static void readLines(File file, Charset charset, Consumer<String> consumer) {
		if (checkFileAccessError(file))
			return;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, charset))) {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				consumer.accept(line);
			}
		}
		catch (IOException e) {
			logger.error("文件读取异常", e);
		}
	}

	/**
	 * 以指定的编码格式读取文本内容，编码为文件本身的编码格式，
	 * @param file
	 * @param charset
	 * @return
	 */
	public static String readContent(File file, Charset charset) {
		if (checkFileAccessError(file))
			return null;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, charset))) {
			StringJoiner joiner = new StringJoiner("\n");
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				joiner.add(line);
			}
			return joiner.toString();
		}
		catch (IOException e) {
			logger.error("文件读取异常", e);
		}
		return null;
	}

	/**
	 * 以指定的编码格式输出文本内容
	 * @param path
	 * @param lines
	 * @param charset
	 * @param append
	 */
	public static void writeLines(Path path, List<String> lines, Charset charset, boolean append) {
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toFile(), charset, append))) {
			for (String line : lines) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
		}
		catch (IOException e) {
			throw new FileOperationException(e);
		}
	}

	/**
	 * 以指定的编码格式输出文本内容
	 * @param path
	 * @param content
	 * @param charset
	 * @param append
	 */
	public static void writeContent(Path path, String content, Charset charset, boolean append) {
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toFile(), charset, append))) {
			bufferedWriter.write(content);
			bufferedWriter.flush();
		}
		catch (IOException e) {
			throw new FileOperationException(e);
		}
	}

}
