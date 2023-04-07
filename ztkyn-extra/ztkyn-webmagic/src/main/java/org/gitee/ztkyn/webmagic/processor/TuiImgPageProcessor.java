package org.gitee.ztkyn.webmagic.processor;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gitee.ztkyn.core.function.DataProcessHandler;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/2 15:46
 */
public class TuiImgPageProcessor implements PageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(TuiImgPageProcessor.class);

	private final Site site = Site.me().setRetryTimes(3).setSleepTime(100);

	private static final String regexPageUrl = "https:\\/\\/www\\.tuiimg\\.com\\/meinv/list_\\d+\\.html";

	private static final String regexDetailUrl = "https:\\/\\/www\\.tuiimg\\.com\\/meinv/\\d+\\/";

	private static final String regexDetailParam = "\\[\\d+,\"(?<fileUrl>\\d+/\\d+/)\",\\d+,(?<total>\\d+),\\d+,\\d+]";

	private static final String imgHost = "https://i.tuiimg.net/";

	@Override
	public void process(Page page) {
		if (page.getUrl().regex(regexPageUrl).match()) {
			// 列表页
			page.addTargetRequests(page.getHtml().links().regex(regexDetailUrl).all());
			page.addTargetRequests(page.getHtml().links().regex(regexPageUrl).all());
		}
		else if (page.getUrl().regex(regexDetailUrl).match()) {
			// 详情页
			String title = parseContent(page, "//*[@id=\"main\"]/h1", "<h1>(?<title>.*)</h1>", "title");
			String date = parseContent(page, "//*[@id=\"main\"]/p/span[1]", "<span>(?<date>.*)</span>", "date");
			// 获取图片链接
			Matcher matcher = Pattern.compile(regexDetailParam).matcher(page.getRawText());
			while (matcher.find()) {
				String fileUrl = matcher.group("fileUrl");
				if (StringUtil.isNotBlank(fileUrl)) {
					DataProcessHandler.of(matcher.group("total"), StringUtil::isNotBlank).ifTrue(s -> {
						Set<String> imgUrlSet = new TreeSet<>();
						for (int i = 1; i <= Integer.parseInt(s); i++) {
							String imgUrl = imgHost + fileUrl + i + ".jpg";
							imgUrlSet.add(imgUrl);
						}
						page.putField(fileUrl, new ImgDetail(fileUrl, title, date, imgUrlSet));
					});
				}
			}
		}
		else {
			// 首页
			page.addTargetRequests(page.getHtml().links().regex(regexDetailUrl).all());
			page.addTargetRequests(page.getHtml().links().regex(regexPageUrl).all());
		}
	}

	private static String parseContent(Page page, String xpath, String regex, String group) {
		String content = page.getHtml().xpath(xpath).get();
		Matcher dateMatcher = Pattern.compile(regex).matcher(content);
		while (dateMatcher.find()) {
			content = dateMatcher.group(group);
		}
		return content;
	}

	@Override
	public Site getSite() {
		return site;
	}

	/**
	 * 结果记录
	 *
	 * @param fileUrl
	 * @param title
	 * @param date
	 * @param imgUrl
	 */
	record ImgDetail(String fileUrl, String title, String date, Set<String> imgUrl) {
	}

	public static void main(String[] args) {
		Spider.create(new TuiImgPageProcessor())
			.addUrl("https://www.tuiimg.com/meinv/")
			.addPipeline(new JsonFilePipeline("/www.tuiimg.com"))
			.thread(1)
			.run();
	}

}
