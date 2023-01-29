package org.gitee.ztkyn.core.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 文件工具类
 * @date 2023/1/16 13:15
 */
public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 系统默认编码，与OS相关
	 */
	public static final Charset defaultCharset = Charset.defaultCharset();

	public static final Charset UTF8 = StandardCharsets.UTF_8;

	public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

	public static final Charset GBK = Charset.forName("GBK");

	public static final Charset GB2312 = Charset.forName("GB2312");

	public static final Charset GB18030 = Charset.forName("GB18030");

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
	 * 将字符串转换成指定编码格式
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String unicodeConvert(String str, Charset charset) {
		return new String(str.getBytes(charset), charset);

	}

}
