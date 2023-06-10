package cn.schff.dyvlog.controller;

import cn.schff.dyvlog.config.MinioConfig;
import cn.schff.dyvlog.common.util.MinIOUtils;
import cn.schff.dyvlog.common.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @Author：眭传洪
 * @Create：2023/5/5 21:32
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Slf4j
@Api(tags = "文件上传模块儿")
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private MinioConfig minioConfig;

    @ApiOperation("文件上传测试接口")
    @PostMapping("/upload")
    public Result fileLoadTest(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        // 上传文件
        MinIOUtils.uploadFile(minioConfig.getBucketName(), fileName, inputStream);
        String imgUrl = minioConfig.getEndPoint() + "/" + minioConfig.getBucketName() + "/" + fileName;
        log.debug("{}", imgUrl);
        inputStream.close();
        return Result.success(imgUrl);
    }
}
