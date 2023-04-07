package org.gitee.ztkyn.webmagic.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author whty
 * @version 1.0
 * @description IDEA 注册码解析地址
 * @date 2023/3/7 13:32
 */
public class IDEAKeyPageProcessor implements PageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(IDEAKeyPageProcessor.class);

	private final Site site = Site.me().setRetryTimes(3).setSleepTime(100);

	@Override
	public void process(Page page) {
		page.getHtml().$("article.card").nodes().forEach(selectable -> {
			Document document = Jsoup.parse(selectable.get());
			Element title = document.selectFirst("div>h1.truncate");
			System.out.println("### " + title.attr("title"));
			Element card = document.selectFirst(".card");
			Element version = document.selectFirst("button.toggle");
			System.out.println("> 注册码标注位: " + card.attr("data-sequence") + " > " + version.attr("data-version"));
		});
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new IDEAKeyPageProcessor())
			.addUrl("https://bafybeiatyghkzrrtodzt3stm652rkrjxndg4hq2ublfdmifk7plg5k5brq.ipfs.dweb.link/")
			.thread(1)
			.run();
	}

}
