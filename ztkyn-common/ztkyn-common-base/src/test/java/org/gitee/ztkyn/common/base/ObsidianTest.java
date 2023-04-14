package org.gitee.ztkyn.common.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.gitee.ztkyn.common.base.collection.ZtkynListUtil;
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

    List<File> mdFileList = ZtkynListUtil.createFastList();

    @Test
    public void readAttachments() throws IOException {
        String obsidianPath = "E:\\Code\\AAA_AliCode\\KM_Obsidian";
        Arrays.stream(Paths.get(obsidianPath).toFile().listFiles(pathname -> {
            return pathname.isDirectory() && pathname.getName().startsWith("KM_");
        })).forEach(file -> {
            listMDFile(file);
        });
        Pattern pattern = Pattern.compile("]\\(attachments/");
        mdFileList.forEach(file -> {
            List<String> utf8Lines = ZtkynListUtil.createFastList();
            Iterator<String> iterator = FileUtil.readUTF8Lines(file).iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (pattern.matcher(next).find()) {
                    next = next.replace("(attachments/", "(/attachments/");
                }
                utf8Lines.add(next);
            }
            cn.hutool.core.io.FileUtil.writeUtf8Lines(utf8Lines, file);
        });
    }

    public void listMDFile(File file) {
        if (file.isFile()) {
            if (file.canRead() && file.getName().endsWith(".md")) {
                mdFileList.add(file);
            }
        } else if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                listMDFile(listFile);
            }
        }
    }

}
