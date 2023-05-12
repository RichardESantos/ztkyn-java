package org.gitee.ztkyn.web.domain.example;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-12 11:32
 * @description OSSRequestVo
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class OSSRequestVo {

	@Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED)
	private Integer code;

	@Schema(description = "目录")
	private String folder;

}
