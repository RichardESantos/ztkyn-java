package org.gitee.ztkyn.web.controller.demo;

import java.util.concurrent.TimeUnit;

import cn.hutool.core.lang.id.NanoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.gitee.ztkyn.web.common.config.RestResponse;
import org.gitee.ztkyn.web.common.config.limit.RequestLimit;
import org.gitee.ztkyn.web.common.config.validation.Update;
import org.gitee.ztkyn.web.common.domain.ResponseResult;
import org.gitee.ztkyn.web.domain.example.UserDTO;
import org.gitee.ztkyn.web.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.gitee.ztkyn.web.common.config.cache.CacheConstants.DEFAULT_CACHE;

/**
 * @author whty
 * @version 1.0
 */
@Validated
@Tag(name = "参数校验模板(restController)", description = "restController 模板")
@RestController
@RequestMapping("/validRestController")
@RestResponse
public class ValidRestController {

	private static final Logger logger = LoggerFactory.getLogger(ValidRestController.class);

	@Resource
	private IndexService indexService;

	/**
	 * requestParam/PathVariable参数校验 GET请求一般会使用requestParam/PathVariable传参。如果参数比较多 (比如超过 6
	 * 个)，还是推荐使用DTO对象接收。否则，推荐将一个个参数平铺到方法入参中。在这种情况下，必须在Controller类上标注@Validated注解，并在入参上声明约束注解
	 * (如@Min等)。如果校验失败，会抛出ConstraintViolationException异常。
	 */

	/**
	 * // @PathVariable 参数校验
	 * @param num
	 * @return
	 */
	@Parameter(name = "num", description = "", in = ParameterIn.PATH, required = true)
	@Operation(summary = "@PathVariable 参数校验", description = "")
	@GetMapping("/{num}")
	public Integer detail(@PathVariable("num") @Min(1) @Max(20) Integer num) {
		return num * num;
	}

	/**
	 * // @RequestParam 参数校验
	 * @param email
	 * @return
	 */
	@Parameter(name = "email", description = "", in = ParameterIn.QUERY, required = true)
	@Operation(summary = "@RequestParam 参数校验", description = "")
	@GetMapping("/getByEmail")
	public String getByAccount(@RequestParam @NotBlank @Email String email) {
		return email;
	}

	// ----------------------------------------------------
	/**
	 * POST、PUT请求一般会使用requestBody传递参数，这种情况下，后端使用 DTO 对象进行接收。只要给 DTO
	 * 对象加上@Validated注解就能实现自动参数校验。 这种情况下，使用@Valid和@Validated都可以。
	 */

	/**
	 * @param userDTO
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	@RestResponse
	public ResponseResult<?> saveUser(@RequestBody @Validated UserDTO userDTO) {
		return ResponseResult.success();
	}

	/**
	 * 分组校验
	 * @param userDTO
	 * @return
	 */
	@PostMapping("/update")
	@ResponseBody
	@RestResponse
	public ResponseResult<?> updateUser(@RequestBody @Validated(Update.class) UserDTO userDTO) {
		return ResponseResult.success();
	}

	/**
	 * //@Cacheable：表示该方法支持缓存。当调用被注解的方法时，如果对应的键已经存在缓存，则不再执行方法体，而从缓存中直接返回。
	 * 当方法返回null时，将不进行缓存操作。cacheNames 对应的缓存必须存在，否则会报异常
	 * //@CachePut：表示执行该方法后，其值将作为最新结果更新到缓存中，每次都会执行该方法。 //@CacheEvict：表示执行该方法后，将触发缓存清除操作。
	 * //@Caching：用于组合前三个注解，例如：
	 */
	/**
	 * 使用缓存
	 * @param requestId
	 * @return
	 */
	@Cacheable(DEFAULT_CACHE)
	@GetMapping("/getNum")
	@ResponseBody
	public String getNum(Integer requestId) {
		return NanoId.randomNanoId();
	}

	/**
	 * 测试在非接口层进行缓存操作--使用缓存
	 * @param requestId
	 * @return
	 */
	@GetMapping("/getNumWithService")
	@ResponseBody
	public String getNumWithService(Integer requestId) {
		return indexService.testCachePut(requestId);
	}

	/**
	 * 清除缓存
	 * @param requestId
	 * @return
	 */
	@CacheEvict(DEFAULT_CACHE)
	@GetMapping("/removeNum")
	@ResponseBody
	public ResponseResult<?> removeNum(Integer requestId) {
		return ResponseResult.success();
	}

	/**
	 * 测试在非接口层进行缓存操作--清除缓存
	 * @param requestId
	 * @return
	 */
	@GetMapping("/removeNumWithService")
	@ResponseBody
	public ResponseResult<?> removeNumWithService(Integer requestId) {
		indexService.testCacheEvict(requestId);
		return ResponseResult.success();
	}

	/**
	 * 测试单机限流
	 * @return
	 */
	@GetMapping("/test2")
	@RequestLimit(key = "limit2", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,
			msg = "当前排队人数较多，请稍后再试！")
	public String limit2() {
		logger.info("令牌桶limit2获取令牌成功");
		return "ok";
	}

	/**
	 * 测试单机限流
	 * @return
	 */
	@GetMapping("/test3")
	@RequestLimit(permitsPerSecond = 2, timeout = 500, timeunit = TimeUnit.MILLISECONDS, msg = "系统繁忙，请稍后再试！")
	public String limit3() {
		logger.info("令牌桶limit3获取令牌成功");
		return "ok";
	}

}
