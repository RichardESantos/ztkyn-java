package org.gitee.ztkyn.web.service;

/**
 * @author whty
 * @version 1.0
 */
public interface IndexService {

	/**
	 * 测试在方法上进行缓存
	 * @param requestId
	 */
	String testCachePut(Integer requestId);

	/**
	 * 测试在方法上删除缓存
	 * @param requestId
	 */
	void testCacheEvict(Integer requestId);

}
