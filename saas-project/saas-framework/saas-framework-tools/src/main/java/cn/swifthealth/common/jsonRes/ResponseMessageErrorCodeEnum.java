package cn.swifthealth.common.jsonRes;

import lombok.AllArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
public enum ResponseMessageErrorCodeEnum implements Serializable {

    SUCCESS(0),
    ARGUMENT_NOTVALID_ERROR(-2),
    SYSTEM_INNER_ERROR(-1);

    private Integer code;

    ResponseMessageErrorCodeEnum(int i) {
        this.code = i;
    }

    public Integer getCode() {
        return this.code;
    }
}
