package org.gitee.ztkyn.web.controller.demo;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.gitee.ztkyn.common.base.collection.ECollectionUtil;
import org.gitee.ztkyn.web.common.domain.ResponseResult;
import org.gitee.ztkyn.web.domain.example.OSSRequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-11 16:48
 * @description FileUploadController
 */
@Tag(name = "文件上传", description = "FileUploadController")
@RequestMapping("/file")
@Controller
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Operation(description = "OSS 文件上传", summary = "OSS 文件上传")
	@PostMapping(value = "/oss", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseResult<String> oss(@RequestPart MultipartFile file, OSSRequestVo ossRequest) {
		logger.info("req:{}", ossRequest.toString());
		logger.info("file-name:{}", file.getOriginalFilename());
		return ResponseResult.success(RandomUtil.randomString(12));
	}

	@Operation(description = "单纯文件上传，无任何参数", summary = "单纯文件上传")
	@PostMapping("/upload")
	public ResponseResult<String> upload(@RequestParam("file") MultipartFile file) {
		return ResponseResult.success(RandomUtil.randomString(12));
	}

	@Operation(summary = "文件上传-带参数")
	@Parameters({
			@Parameter(name = "file", description = "文件", required = true, in = ParameterIn.DEFAULT, ref = "file"),
			@Parameter(name = "name", description = "文件名称", required = true), })
	@PostMapping("/uploadParam")
	public ResponseResult<String> uploadParam(@RequestParam("file") MultipartFile file,
			@RequestParam("name") String name) {
		return ResponseResult.success(RandomUtil.randomString(12));
	}

	@Operation(summary = "文件上传-带参数Header")
	@Parameters({ @Parameter(name = "token", description = "请求token", required = true, in = ParameterIn.HEADER),
			@Parameter(name = "file", description = "文件", required = true, in = ParameterIn.DEFAULT, ref = "file"),
			@Parameter(name = "name", description = "文件名称", required = true), })
	@PostMapping("/uploadParamHeader")
	public ResponseResult<String> uploadParamHeader(@RequestHeader("token") String token,
			@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
		return ResponseResult.success(RandomUtil.randomString(12));
	}

	@Operation(summary = "文件上传-带参数Path")
	@Parameters({ @Parameter(name = "id", description = "文件id", in = ParameterIn.PATH),
			@Parameter(name = "file", description = "文件", required = true, in = ParameterIn.DEFAULT, ref = "file"),
			@Parameter(name = "name", description = "文件名称", required = true), })
	@PostMapping("/uploadParam/{id}")
	public ResponseResult<String> uploadParamPath(@PathVariable("id") String id,
			@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
		return ResponseResult.success(RandomUtil.randomString(12));
	}

	@Operation(summary = "多文件上传")
	@Parameter(name = "file", description = "文件", in = ParameterIn.DEFAULT, ref = "file")
	@PostMapping("/uploadBatch")
	public ResponseResult<List<String>> uploadBatch(@RequestParam("files") MultipartFile[] files) {
		return ResponseResult.success(ECollectionUtil.createFastList());
	}

}
