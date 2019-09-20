package com.weChatCard.controller;

import com.weChatCard.config.LoginRequired;
import com.weChatCard.entities.User;
import com.weChatCard.utils.CommonResponse;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

/**
 * 文件上传restful接口类
 *
 * @Author: yupeng
 */

@RestController
@RequestMapping("/uploadFile")
@Api(description = "上传文件")
@LoginRequired
public class UploadFileController {

    private static Logger log = LoggerFactory.getLogger(UploadFileController.class);

    @Value("${UPLOAD_FILE_PATH}")
    private String uploadFilePath;

    @PostMapping(path = "/upload")
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public CommonResponse upload(@NotNull(message = "上传文件不能为空") @RequestParam("file") MultipartFile mfile, @RequestParam("name") String originFileName, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        Calendar c = Calendar.getInstance();
        // 每个月生成一个文件夹来放文件
        String timePath = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH);
        String path = this.uploadFilePath + File.separator + loginUser.getUserName() + File.separator + timePath;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        log.info("文件上传：" + originFileName);
        if (mfile.isEmpty() ) {
            commonResponse.setCode(Messages.CODE_40001);
            commonResponse.setMessage("上传文件为空");
        } else {
            String suffix = originFileName.split("\\.")[originFileName.split("\\.").length - 1];
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
            File file = new File(path, fileName);
            try {
                FileUtils.copyInputStreamToFile(mfile.getInputStream(), file);
                String uploadFileName = originFileName + "|" + loginUser.getUserName() + "/" + timePath + "/" + fileName;
                commonResponse.setResult(uploadFileName);
            } catch (IOException e) {
                e.printStackTrace();
                commonResponse.setCode(Messages.CODE_50000);
                commonResponse.setMessage(e.getMessage());
            }
        }
        return commonResponse;
    }
}
