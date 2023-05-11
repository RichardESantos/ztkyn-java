package org.gitee.ztkyn.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.gitee.ztkyn.web.common.domain.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-11 16:48
 * @description FileUploadController
 */
@Tag(name = "/file", description = "FileUploadController")
@RequestMapping("/file")
@Controller
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Parameters({
            @Parameter(name = "request", description = "", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "files", description = "", in = ParameterIn.QUERY, required = true)
    })
    @Operation(summary = "", description = "")
    @PostMapping("/import/mufile")
    @ResponseBody
    public ResponseResult<Object> importMulFile(HttpServletRequest request,
                                                @RequestParam(value = "file", required = true) MultipartFile[] files) {
        return ResponseResult.success();
    }

    @Parameters({
            @Parameter(name = "request", description = "", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "file", description = "", in = ParameterIn.QUERY, required = true)
    })
    @Operation(summary = "", description = "")
    @PostMapping("/import/file")
    @ResponseBody
    public ResponseResult<Object> importFile(HttpServletRequest request,
                                             @RequestParam(value = "file", required = true) MultipartFile file) {
        return ResponseResult.success();
    }
}
