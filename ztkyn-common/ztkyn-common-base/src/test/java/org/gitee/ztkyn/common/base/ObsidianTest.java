package org.gitee.ztkyn.common.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.lang.id.NanoId;
import org.gitee.ztkyn.common.base.collection.ECollectionUtil;
import org.gitee.ztkyn.core.io.FileUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-14 11:06
 * @description ObsidianTest
 */
public class ObsidianTest {

	private static final Logger logger = LoggerFactory.getLogger(ObsidianTest.class);

	List<File> mdFileList = ECollectionUtil.createFastList();

	@Test
	void readAllLines() {
		String path = "D:\\各种文档\\项目文档\\行业知识图谱管理平台BlueKG\\电影数据\\事件抽取数据\\data\\dev.json";
		// FileUtil.readAllLines(path, StandardCharsets.UTF_8);
		path = "F:\\Download\\Edge\\神魔书.txt";

		// try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path,
		// CharsetUtil.GB18030))) {
		// String line = null;
		// while ((line = bufferedReader.readLine()) != null) {
		// System.out.println(line);
		// System.out.println(StringUtil.unicodeConvert(line, StandardCharsets.UTF_8));
		// }
		// }
		// catch (IOException e) {
		// throw new RuntimeException(e);
		// }
	}

	@Test
	public void renamePasteImg() {
		String obsidianPath = "E:\\Code\\AAA_AliCode\\KM_Obsidian";
		Arrays.stream(Paths.get(obsidianPath).toFile().listFiles(pathname -> {
			return pathname.isDirectory() && pathname.getName().startsWith("KM_");
		})).forEach(this::listMDFile);
		List<String> attachments = Arrays
			.stream(Objects.requireNonNull(Paths.get(obsidianPath, "attachments").toFile().list()))
			.toList();
		Pattern attachmentPattern = Pattern.compile("\\(/attachments/(?<fileName>Pasted[A-Za-z0-9_\\s.]+?)\\)");
		mdFileList.forEach(file -> {
			boolean isReplace = false;
			List<String> utf8Lines = ECollectionUtil.createFastList();
			Iterator<String> iterator = FileUtil.readUTF8Lines(file).iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				Matcher matcher = attachmentPattern.matcher(next);
				if (matcher.find()) {
					String fileName = matcher.group("fileName");
					System.out.println(fileName);
					String fileExt = fileName.substring(fileName.lastIndexOf("."));
					String nanoIdFileName = "image-" + NanoId.randomNanoId() + fileExt;
					if (attachments.contains(fileName)) {
						isReplace = true;
						next = next.replace(fileName, nanoIdFileName);
						Paths.get(obsidianPath, "attachments", fileName)
							.toFile()
							.renameTo(Paths.get(obsidianPath, "attachments", nanoIdFileName).toFile());
					}
					System.out.println(next);
				}
				utf8Lines.add(next);
			}
			if (isReplace) {
				cn.hutool.core.io.FileUtil.writeUtf8Lines(utf8Lines, file);
			}
		});
	}

	/**
	 * 确认资源是否存在遗失问题
	 */
	@Test
	public void confirmAttachments() {
		String obsidianPath = "E:\\Code\\AAA_AliCode\\KM_Obsidian";
		Arrays.stream(Paths.get(obsidianPath).toFile().listFiles(pathname -> {
			return pathname.isDirectory() && pathname.getName().startsWith("KM_");
		})).forEach(this::listMDFile);

		List<String> attachments = Arrays
			.stream(Objects.requireNonNull(Paths.get(obsidianPath, "attachments").toFile().list()))
			.toList();
		Pattern attachmentPattern = Pattern.compile("\\(/attachments/(?<fileName>[A-Za-z0-9_\\s.]+?)\\)");
		mdFileList.forEach(file -> {

			Iterator<String> iterator = FileUtil.readUTF8Lines(file).iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				Matcher matcher = attachmentPattern.matcher(next);
				if (matcher.find()) {
					String fileName = matcher.group("fileName").replace("%20", " ");
					if (!attachments.contains(fileName)) {
						System.out.println(file.getName());
						System.out.println(fileName);
					}
				}
			}
		});
	}

	@Test
	public void readAttachments() throws IOException {
		String obsidianPath = "E:\\Code\\AAA_AliCode\\KM_Obsidian";
		Arrays.stream(Paths.get(obsidianPath).toFile().listFiles(pathname -> {
			return pathname.isDirectory() && pathname.getName().startsWith("KM_");
		})).forEach(this::listMDFile);
		Pattern resourcePattern = Pattern.compile("!\\[");
		Pattern attachmentPattern = Pattern.compile("\\(/attachments/");
		Pattern wikiPattern = Pattern.compile("!\\[\\[(?<fileName>[\\S\\s]*?)]]");
		mdFileList.forEach(file -> {
			boolean isReplace = false;
			List<String> utf8Lines = ECollectionUtil.createFastList();
			Iterator<String> iterator = FileUtil.readUTF8Lines(file).iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				if (resourcePattern.matcher(next).find() && !attachmentPattern.matcher(next).find()) {
					System.out.println(next);
					// wiki 格式
					Matcher wikiMatcher = wikiPattern.matcher(next);
					if (wikiMatcher.find()) {
						isReplace = true;
						String fileName = wikiMatcher.group("fileName");
						next = next.replace(wikiMatcher.group(), "![](/attachments/" + fileName + ")");
					}
					else {
						isReplace = true;
						next = next.replace("](", "](/attachments/");
					}
					// next = next.replace("(attachments/", "(/attachments/");
				}
				utf8Lines.add(next);
			}
			if (isReplace) {
				// cn.hutool.core.io.FileUtil.writeUtf8Lines(utf8Lines, file);
			}
		});
	}

	public void listMDFile(File file) {
		if (file.isFile()) {
			if (file.canRead() && file.getName().endsWith(".md")) {
				mdFileList.add(file);
			}
		}
		else if (file.isDirectory()) {
			for (File listFile : Objects.requireNonNull(file.listFiles())) {
				listMDFile(listFile);
			}
		}
	}

}
