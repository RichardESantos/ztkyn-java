package org.gitee.ztkyn.web.common.service;

import java.util.List;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-19 15:17
 * @description ZtkynUserInfoService
 */
public interface ZtkynUserInfoService<ENTITY, ID> {

	ENTITY getUserInfo(String userName);

	ID getUserId();

	String getUserName();

	String getPasswd();

	List<String> getRoleCodeList();

}
