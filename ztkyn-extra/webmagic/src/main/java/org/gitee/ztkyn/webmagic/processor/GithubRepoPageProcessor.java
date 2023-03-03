package org.gitee.ztkyn.webmagic.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/2 15:31
 */
public class GithubRepoPageProcessor implements PageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(GithubRepoPageProcessor.class);

	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

	@Override
	public void process(Page page) {
		page.addTargetRequests(page.getHtml().links().regex("(https://hub\\.yzuu\\.cf/\\w+/\\w+)").all());
		page.putField("author", page.getUrl().regex("https://hub\\.yzuu\\.cf/(\\w+)/.*").toString());
		page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
		if (page.getResultItems().get("name") == null) {
			// skip this page
			page.setSkip(true);
		}
		page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new GithubRepoPageProcessor()).addUrl("https://hub.yzuu.cf/code4craft").thread(5).run();
	}

}
