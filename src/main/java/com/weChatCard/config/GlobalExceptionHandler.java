package com.weChatCard.config;

import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.exception.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一异常处理
 *
 * @Author: yupeng
 */

@ControllerAdvice
@PropertySource("classpath:application.yml")
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ExceptionResponse bindingErrorHandler(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        BindingResult bindingResult = e.getBindingResult();

        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

        List<String> message = new ArrayList<String>();
        for (FieldError fieldError : fieldErrorList) {
            message.add(formatStr(fieldError.getField()) + ":" + fieldError.getDefaultMessage());
        }
        exceptionResponse.setErrorCode(ExceptionResponse.BINDING_ERROR_CODE);
        exceptionResponse.setMessage(ExceptionResponse.BINDING_ERROR_MESSAGE);
        exceptionResponse.setErrorData(message.toString());

        //sendMain(e);
        return exceptionResponse;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ExceptionResponse businessErrorHandler(BusinessException e) throws Exception {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setErrorCode(e.getCode());
        exceptionResponse.setMessage(e.getMessage());

        //sendMain(e);
        return exceptionResponse;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ExceptionResponse commonErrorHandler(Exception e) throws Exception {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setErrorCode(ExceptionResponse.SYSTEM_ERROR_CODE);
        exceptionResponse.setMessage(ExceptionResponse.SYSTEM_ERROR_MESSAGE);

        //sendMain(e);
        return exceptionResponse;
    }

    private String formatStr(String str) {
        StringBuilder sbf = new StringBuilder("");
        for (int i = 0; i < str.length(); i++) {
            char tempChr = str.charAt(i);
            if (tempChr >= 'A' && tempChr <= 'Z') {
                sbf.append("_");
            }
            sbf.append(tempChr);
        }
        return sbf.toString().toLowerCase();
    }
}
