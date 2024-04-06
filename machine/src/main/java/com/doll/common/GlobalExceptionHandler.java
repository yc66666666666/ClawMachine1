package com.doll.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
   public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());
        if (exception.getMessage().contains("Duplicate entry")){
            String[] split=exception.getMessage().split(" ");
            String msg=split[2]+"已经存在";
            return  R.error(msg);
        }
        return  R.error("未知错误");
    }

    @ExceptionHandler(CustomerException.class)
    public R<String> exceptionHandler(CustomerException exception){
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }
}
