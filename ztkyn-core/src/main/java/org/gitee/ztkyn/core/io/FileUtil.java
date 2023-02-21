package org.gitee.ztkyn.core.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.gitee.ztkyn.core.string.CharsetUtil.UTF8;

/**
 * @author whty
 * @version 1.0
 * @description 文件工具类
 * @date 2023/1/16 13:15
 */
public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static String readUTF8Content(File file) {
		return readContent(file, UTF8);
	}

	public static String readUTF8Content(String path) {
		return readContent(Paths.get(path).toFile(), UTF8);
	}

	public static List<String> readUTF8Lines(File file) {
		return readAllLines(file, UTF8);
	}

	public static List<String> readUTF8Lines(String path) {
		return readAllLines(path, UTF8);
	}

	public static List<String> readAllLines(String path, Charset charset) {
		return readAllLines(Paths.get(path).toFile(), charset);
	}

	public static void readUTF8Lines(File file, Consumer<String> consumer) {
		readLines(file, UTF8, consumer);
	}

	/**
	 * 检车文件是否能正常读取
	 * @param file
	 * @return
	 */
	public static boolean checkFileAccessError(File file) {
		String path = file.getAbsolutePath();
		if (!file.exists()) {
			logger.warn("文件读取失败，【{}】文件不存在", path);
			return true;
		}
		if (!file.isFile()) {
			logger.warn("文件读取失败，【{}】不是文件", path);
			return true;
		}
		if (!file.canRead()) {
			logger.warn("文件读取失败，【{}】权限不足", path);
			return true;
		}
		return false;
	}

	/**
	 * 以指定的编码格式读取文本内容，编码与文本本身的编码格式不一致也没有问题，
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
	 * 以指定的编码格式读取文本内容，采用边读取边消费的模式，减少内存占用 编码与文本本身的编码格式不一致也没有问题，
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
	 * 都去全部内容
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

}
