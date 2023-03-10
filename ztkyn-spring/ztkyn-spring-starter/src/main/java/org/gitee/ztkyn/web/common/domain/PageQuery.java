package org.gitee.ztkyn.web.common.domain;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 分页查询参数
 * @date 2023/3/10 13:30
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PageQuery<T> {

	private static final Logger logger = LoggerFactory.getLogger(PageQuery.class);

	public static final int DEFAULT_PAGE_SIZE = 10;

	public static final int DEFAULT_PAGE_NUM = 1;

	@Schema(description = "分页大小", example = "10")
	@Range(min = 1, max = 1000, message = "每页条数，取值范围 1-1000")
	private Integer pageSize;

	@Schema(description = "当前页页码,从1开始", example = "1")
	@Min(value = 1, message = "页码最小值为 1")
	private Integer pageNum;

	@Schema(description = "查询参数")
	private T queryParams;

	public static <T> PageQuery<T> of(T queryParams) {
		return new PageQuery<T>().setQueryParams(queryParams).setPageSize(DEFAULT_PAGE_SIZE)
				.setPageNum(DEFAULT_PAGE_NUM);
	}

	public static <T> PageQuery<T> of(T queryParams, Integer pageNum) {
		return new PageQuery<T>().setQueryParams(queryParams).setPageSize(DEFAULT_PAGE_SIZE).setPageNum(pageNum);
	}

	public static <T> PageQuery<T> of(T queryParams, Integer pageNum, Integer pageSize) {
		return new PageQuery<T>().setQueryParams(queryParams).setPageSize(pageSize).setPageNum(pageNum);
	}

	@Deprecated
	public Integer getPageSize() {
		return pageSize;
	}

	@Deprecated
	public Integer getPageNum() {
		return pageNum;
	}

	/**
	 * 替代 getPageNum ,避免 Null
	 * @return
	 */
	public Integer getPageNumOrDefault() {
		return Objects.nonNull(pageNum) && pageNum > 1 ? pageNum : DEFAULT_PAGE_NUM;
	}

	/**
	 * 替代 getPageSize ,避免 Null
	 * @return
	 */
	public Integer getPageSizeOrDefault() {
		return Objects.nonNull(pageSize) && (pageSize >= 1 && pageSize <= 1000) ? pageSize : DEFAULT_PAGE_SIZE;
	}

}
