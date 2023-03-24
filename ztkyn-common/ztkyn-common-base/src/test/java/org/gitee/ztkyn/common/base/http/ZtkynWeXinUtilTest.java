package org.gitee.ztkyn.common.base.http;

import java.text.MessageFormat;

import org.junit.jupiter.api.Test;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/23 14:56
 */
class ZtkynWeXinUtilTest {

	@Test
	void getAlbumArticles() {
		String url = "https://mp.weixin.qq.com/mp/appmsgalbum?__biz=Mzg2OTA0Njk0OA==&action=getalbum&album_id=1352302538565189634&scene=173&from_msgid=2247533882&from_itemidx=1&count=3&nolastread=1#wechat_redirect";
		ZtkynWeXinUtil.getAlbumArticles(url).forEach(albumArticle -> {
			System.out.println(MessageFormat.format("[{0}]({1})", albumArticle.getTitle(), albumArticle.getUrl()));
		});
	}

}