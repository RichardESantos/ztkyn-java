package org.gitee.ztkyn.core.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import org.gitee.ztkyn.core.exception.FileOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.gitee.ztkyn.core.string.CharsetUtil.ISO_8859_1;
import static org.gitee.ztkyn.core.string.CharsetUtil.UTF8;

/**
 * @author richard
 * @version 1.0
 * @description
 * @date 2023/2/20 14:18
 */
public class RandomAccessFileUtil {

	private static final Logger logger = LoggerFactory.getLogger(RandomAccessFileUtil.class);

	public static List<String> readUTF8Lines(String filePath) {
		return readLines(new File(filePath), UTF8);
	};

	public static List<String> readUTF8Lines(File file) {
		return readLines(file, UTF8);
	};

	public static List<String> readLines(File file, Charset charset) {
		return readLines(file, charset, null);
	}

	public static List<String> readLines(File file, Charset charset, Consumer<String> consumer) {
		List<String> contentList = new ArrayList<>();
		if (FileUtil.checkFileAccessError(file)) {
			return contentList;
		}
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
			String line = null;
			while ((line = randomAccessFile.readLine()) != null) {
				// 将readLine读取出来的字符串通过getBytes方法按iso-8859-1编码成字节数组，然后再将字节数组按gbk解码，最后转换成字符串输出。
				line = new String(line.getBytes(ISO_8859_1), charset);
				contentList.add(line);
				// 边解析边执行指定操作
				if (null != consumer) {
					consumer.accept(line);
				}
			}
		}
		catch (IOException e) {
			throw new FileOperationException(e);
		}
		return contentList;
	}

	/**
	 * 查找指定内容，并执行特定操作，返回处理之后的结果值
	 * @param file
	 * @param regex
	 * @param replacer
	 */
	public static void findContentAndThen(File file, String regex, Function<MatchResult, String> replacer) {
		if (FileUtil.checkFileAccessError(file)) {
			throw new FileOperationException("文件读取失败");
		}
		Path path = Paths.get(file.getAbsolutePath() + "_tmp");
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toFile()))) {
			String line = null;
			Pattern pattern = Pattern.compile(regex);
			while ((line = randomAccessFile.readLine()) != null) {
				// 将readLine读取出来的字符串通过getBytes方法按iso-8859-1编码成字节数组，然后再将字节数组按gbk解码，最后转换成字符串输出。
				line = new String(line.getBytes(ISO_8859_1), UTF8);
				bufferedWriter.write(pattern.matcher(line).replaceAll(replacer));
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
		}
		catch (IOException e) {
			throw new FileOperationException(e);
		}
		try {
			// 用临时文件覆盖就文件
			Files.copy(path, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			// 删除临时文件
			Files.delete(path);
		}
		catch (IOException e) {
			throw new FileOperationException(e);
		}
	}

}
