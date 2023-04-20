package org.gitee.ztkyn.common.base.http;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.gitee.ztkyn.common.base.JacksonUtil;
import org.gitee.ztkyn.common.base.collection.ZtkynListUtil;
import org.gitee.ztkyn.common.base.http.ZtkynUrlUtil.UrlContent;
import org.gitee.ztkyn.core.exception.AssertUtil;
import org.gitee.ztkyn.core.function.DataProcessHandler;
import org.gitee.ztkyn.core.function.PredicateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 微信工具类
 * @date 2023/3/23 10:59
 */
public class ZtkynWeXinUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynWeXinUtil.class);

	/**
	 * 获取专栏文章列表
	 * @param albumUrl
	 * @return
	 */
	public static List<AlbumArticle> getAlbumArticles(String albumUrl) {
		List<AlbumArticle> albumArticleList = ZtkynListUtil.createFastList();
		// 获取到专栏首页
		String albumPageHtml = ZtkynOkHttpsUtil.get(albumUrl);
		Document albumDoc = Jsoup.parse(albumPageHtml);
		Iterator<Element> iterator = albumDoc.select(" div.album__content.js_album_bd > ul > li").iterator();
		AtomicReference<String> msgId = new AtomicReference<>();
		while (iterator.hasNext() && Objects.isNull(msgId.get())) {
			Element li = iterator.next();
			DataProcessHandler.of(li.attr("data-msgid"), PredicateUtil.strNotBlank).ifTrue(msgId::set);
		}

		// 获取剩下的内容
		DataProcessHandler.of(ZtkynUrlUtil.parseUrl(albumUrl), Objects::nonNull).ifTrue(urlContent -> {
			Map<String, Object> paramsMap = urlContent.getParamsMap();
			paramsMap.remove("from_msgid");
			paramsMap.remove("from_itemidx");
			paramsMap.remove("scene");
			paramsMap.remove("nolastread");

			paramsMap.put("begin_itemidx", 1);
			paramsMap.put("count", 20);
			paramsMap.put("x5", 0);
			paramsMap.put("f", "json");

			// 循环获取剩下列表
			do {
				try {
					getRestAlbumList(albumArticleList, msgId, urlContent, paramsMap);
					TimeUnit.SECONDS.sleep(3);
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			while (Objects.nonNull(msgId.get()));

		});
		return albumArticleList;
	}

	private static void getRestAlbumList(List<AlbumArticle> albumArticleList, AtomicReference<String> msgId,
			UrlContent urlContent, Map<String, Object> paramsMap) {
		String curMsgId = msgId.get();
		paramsMap.put("begin_msgid", curMsgId);
		msgId.set(null);
		String listUrl = ZtkynUrlUtil.genPathUrl(urlContent.getBaseUrl(), paramsMap);
		logger.info("消息ID:{}，请求当前分页地址:{}", curMsgId, listUrl);
		DataProcessHandler.of(ZtkynOkHttpsUtil.get(listUrl), PredicateUtil.strNotBlank).ifTrue(s -> {
			logger.info("消息ID:{}，请求当前分页结果:{}", curMsgId, s);
			AlbumPage albumPage = JacksonUtil.json2Obj(s, AlbumPage.class);
			if (Objects.isNull(albumPage))
				return;
			AlbumResp albumResp = albumPage.getAlbumResp();
			if (Objects.isNull(albumResp))
				return;
			AssertUtil.objectNotNull(albumResp, "获取结果为空");
			DataProcessHandler.of(albumResp.getArticleList(), PredicateUtil.listNotBlank()).ifTrue(articleList -> {
				articleList.stream().min((o1, o2) -> o1.getMsgid().compareTo(o2.getMsgid())).ifPresent(albumArticle -> {
					msgId.set(albumArticle.getMsgid());
				});
				// 确认是否找到了最后一条
				articleList.stream()
					.filter(albumArticle -> Objects.equals(albumArticle.getPosNum(), "1"))
					.findAny()
					.ifPresent(albumArticle -> {
						msgId.set(null);
					});
				albumArticleList.addAll(articleList);
			});
		});
	}

	@Data
	static class AlbumPage {

		@JsonProperty("getalbum_resp")
		private AlbumResp albumResp;

	}

	@Data
	static class AlbumResp {

		@JsonProperty("article_list")
		private List<AlbumArticle> articleList;

	}

	@Data
	@Accessors(chain = true)
	static class AlbumArticle {

		@JsonProperty("cover_img_1_1")
		private String coverImg11;

		@JsonProperty("create_time")
		private String createTime;

		@JsonProperty("is_pay_subscribe")
		private String isPaySubscribe;

		@JsonProperty("is_read")
		private String isRead;

		private String itemidx;

		private String key;

		private String msgid;

		@JsonProperty("pos_num")
		private String posNum;

		private String title;

		private String url;

		@JsonProperty("user_read_status")
		private String userReadStatus;

	}

}
