package cn.swifthealth.common.jsonRes;

import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义统一响应体返回消息类
 *
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class ResponseMessage<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMessage.class);
    /**
     * 返回状态码
     */
    private int code;
    /**
     * 状态消息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public ResponseMessage() {
    }

    public ResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
        LOGGER.info(toString());
    }

    public ResponseMessage(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        LOGGER.info(toString());
    }


    public static ResponseMessage success() {
        return new ResponseMessage(ResponseMessageErrorCodeEnum.SUCCESS.getCode(), "", true);
    }

    public static <T> ResponseMessage<T> success(int code, T t) {

        return new ResponseMessage(code, "", t);
    }

    public static <T> ResponseMessage<T> success(int code, String message, T t) {
        return new ResponseMessage(code, message, t);
    }

    public static <T> ResponseMessage<T> success(T t) {
        return new ResponseMessage(ResponseMessageErrorCodeEnum.SUCCESS.getCode(), "", t);
    }

    public static ResponseMessage error() {
        return error("");
    }

    public static ResponseMessage error(String message) {
        return error(ResponseMessageErrorCodeEnum.SYSTEM_INNER_ERROR.getCode(), message);
    }

    public static ResponseMessage error(int code, String message) {
        return error(code, message, null);
    }

    public static <T> ResponseMessage<T> error(int code, String message, T t) {
        return new ResponseMessage(code, message, t);
    }


}
