package org.gitee.ztkyn.boot.framework.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/10 13:55
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PageResult<T> {

	private static final Logger logger = LoggerFactory.getLogger(PageResult.class);

	/**
	 * 空数据页
	 */
	public static final PageResult<?> emptyPage = new PageResult<>().setTotal(0L).setData(Collections.emptyList());

	@Schema(description = "总的记录数")
	private Long total;

	@Schema(description = "当前分页里的数据")
	private List<T> data;

	@Schema(description = "当前分页大小")
	private Integer pageSize;

	@Schema(description = "当前页码")
	private Integer pageNum;

	public static <T> PageResult<T> of(Long total, List<T> data) {
		return new PageResult<T>().setTotal(total).setData(data);
	}

	/**
	 * 使用于 先查询是否存在数据，如果没有则不查询的情况
	 * @param totalSupplier
	 * @param dataSupplier
	 * @param <T>
	 * @return
	 */
	public static <T> PageResult<T> of(Supplier<Long> totalSupplier, Supplier<List<T>> dataSupplier) {
		Long aLong = totalSupplier.get();
		if (Objects.nonNull(aLong) && aLong > 0L) {
			return PageResult.of(aLong, dataSupplier.get());
		}
		return PageResult.of(0L, Collections.emptyList());
	}

	/**
	 * 回填分页信息
	 * @param pageQuery
	 * @param <E>
	 * @return
	 */
	public <E> PageResult<T> withPageQuery(PageQuery<E> pageQuery) {
		return this.setPageSize(pageQuery.getPageSizeOrDefault()).setPageNum(pageQuery.getPageNumOrDefault());
	}

}
