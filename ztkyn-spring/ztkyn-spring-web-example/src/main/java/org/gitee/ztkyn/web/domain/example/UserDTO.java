package org.gitee.ztkyn.web.domain.example;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.gitee.ztkyn.web.common.config.validation.Update;
import org.hibernate.validator.constraints.Length;

/**
 * @author whty
 * @version 1.0
 * @description 在DTO字段上声明约束注解
 * @date 2023/1/29 16:06
 */
@Data
public class UserDTO {

	@NotNull(message = "用户ID不能为空", groups = Update.class)
	private Long userId;

	@NotNull
	@Length(min = 2, max = 10)
	private String userName;

	@NotNull
	@Length(min = 6, max = 20)
	private String account;

	@NotNull
	@Length(min = 6, max = 20)
	private String password;

}