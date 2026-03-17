package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AlyOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {


    private final AlyOSSUtils alyOSSUtils;

    public CommonController(AlyOSSUtils alyOSSUtils) {
        this.alyOSSUtils = alyOSSUtils;
    }

    /**
     * 文件上传
     *
     * @param file 文件
     */
    @PostMapping("upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        log.info("文件上传：{}", file);
        String upload = alyOSSUtils.upload(file.getBytes(), Objects.requireNonNull(file.getOriginalFilename()));
        log.info("文件上传成功：{}", upload);
        return Result.success(upload);
    }

}
