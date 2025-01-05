package cn.swifthealth.common.jsonRes;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private int code;
    private String msg;

    public APIException() {
        this(-1, "接口错误");
    }

    public APIException(String msg) {
        this(-1, msg);
    }

    public APIException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}