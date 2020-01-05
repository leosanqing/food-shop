package com.leosanqing.exception;

import com.leosanqing.utils.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.logging.Handler;

/**
 * @Author: leosanqing
 * @Date: 2019/12/17 上午8:07
 * @Package: com.leosanqing.exception
 * @Description: 自定义异常处理
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public JSONResult HandlerMaxUploadSize(MaxUploadSizeExceededException exception){{
        return JSONResult.errorMsg("上传图片超过500k，请压缩后在上传");
    }

    }
}
