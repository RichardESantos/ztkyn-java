package org.gitee.ztkyn.web.common.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-19 15:15
 * @description ZtkynUserInfo
 */
public abstract class ZtkynUserInfo {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynUserInfo.class);

	public abstract String getUserId();

	public abstract String getUserName();

	public abstract String getPasswd();

}
