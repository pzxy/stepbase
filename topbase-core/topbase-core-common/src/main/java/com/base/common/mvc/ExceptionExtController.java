package com.base.common.mvc;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import com.base.core.head.constants.CodeResVal;
import com.base.core.mvc.web.BaseExceptionController;

/**
 * 全局异常处理
 * @author user
 *
 */
@RestControllerAdvice
public class ExceptionExtController extends BaseExceptionController {
	
    @ExceptionHandler(DuplicateKeyException.class)
    public Object handlerDuplicateKeyException(HandlerMethod handler, DuplicateKeyException e) {
        return response(messageParse(CodeResVal.CODE_10004), handler, e);
    }

}
