package cn.swifthealth.common.jsonRes;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        return ResponseMessage.error(ResponseMessageErrorCodeEnum.ARGUMENT_NOTVALID_ERROR.getCode(), objectError.getDefaultMessage());
    }

    @ExceptionHandler(APIException.class)
    public ResponseMessage APIExceptionHandler(APIException e) {
        return ResponseMessage.error(ResponseMessageErrorCodeEnum.SYSTEM_INNER_ERROR.getCode(), e.getMsg());
    }

}
