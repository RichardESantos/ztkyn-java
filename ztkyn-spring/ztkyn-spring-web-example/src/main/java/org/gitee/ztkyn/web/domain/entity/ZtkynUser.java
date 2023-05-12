package org.gitee.ztkyn.web.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-12 10:49
 * @description ZtkynUser
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ZtkynUser {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynUser.class);

	private Long id;

	private String userName;

	/**
	 * 登录账号
	 */
	private String account;

	private String passwd;

	/**
	 * 描述
	 */
	private String desc;

	private String email;

	private String phoneNum;

	private String address;

	/**
	 * 创建时间
	 */
	private Long createTime;

}
