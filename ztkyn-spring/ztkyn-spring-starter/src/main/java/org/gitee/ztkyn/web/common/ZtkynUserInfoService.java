package org.gitee.ztkyn.web.common;

import org.gitee.ztkyn.web.common.domain.ZtkynUserInfo;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-19 15:17
 * @description ZtkynUserInfoService
 */
public interface ZtkynUserInfoService {

	ZtkynUserInfo getUserInfo(String userName);

}
