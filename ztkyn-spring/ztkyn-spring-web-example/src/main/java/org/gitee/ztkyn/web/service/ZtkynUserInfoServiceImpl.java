package org.gitee.ztkyn.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import org.gitee.ztkyn.web.common.service.ZtkynUserInfoService;
import org.gitee.ztkyn.web.domain.entity.ZtkynUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-12 10:45
 * @description ZtkynUserInfoServiceImpl
 */
@Service
public class ZtkynUserInfoServiceImpl implements ZtkynUserInfoService<ZtkynUser, Long> {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynUserInfoServiceImpl.class);

	@Override
	public ZtkynUser getUserInfo(String userName) {
		return null;
	}

	@Override
	public Long getUserId() {
		return null;
	}

	@Override
	public String getUserName() {
		return null;
	}

	@Override
	public String getPasswd() {
		return null;
	}

	@Override
	public List<String> getRoleCodeList() {
		return null;
	}

}
