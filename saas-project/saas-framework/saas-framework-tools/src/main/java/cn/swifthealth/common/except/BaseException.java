package cn.swifthealth.common.except;

import lombok.Getter;
import lombok.Setter;

/**
 * <p> 基础异常 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 2.21.2
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 2325637322994896102L;

    /**
     * 支持翻译?
     */
    private boolean supportTranslate = true;

    @Getter
    @Setter
    private Integer code;

    @Setter
    private String message;

    /**
     * 异常数据，该数据在异常发生后，会提供一个异常数据返回给前台
     *
     */
    @Getter
    @Setter
    protected Object errorData;

    /**
     * 兼容旧的构造方式，已弃用
     *
     * @param message 消息
     */
    @Deprecated
    public BaseException(final String message) {
        super();
        this.supportTranslate = false;
        this.message = message;
        this.code = -1;
    }

    /**
     * 兼容旧的构造方式，已弃用
     *
     * @param message 消息
     */
    @Deprecated
    public BaseException(final String message, final Object errorData) {
        this(message);
        this.errorData = errorData;
    }

    @Deprecated
    public BaseException(final Integer code, final String message) {
        this(message);
        this.code = code;
    }

    @Deprecated
    public BaseException(final Integer code, final String message, final Object errorData) {
        this(code, message);
        this.errorData = errorData;
    }

    @Deprecated
    public BaseException(final Integer code, final Object errorData, final int messageSourceId, final Object... args) {
        this.code = code;
        this.errorData = errorData;
    }

    @Deprecated
    public BaseException(final Integer code, final Object errorData, final int messageSourceId) {
        this.code = code;
        this.errorData = errorData;
    }

    @Deprecated
    public BaseException(final Integer code, final int messageSourceId) {
        this.code = code;
    }

    @Deprecated
    public BaseException(final Integer code, final int messageSourceId, final Object... args) {
        this.code = code;
    }

    public BaseException(final int messageSourceId, final Object... args) {
        this.code = messageSourceId;
    }

    public BaseException(final int messageSourceId) {
        this.code = messageSourceId;
    }

    /**
     * 携带异常数据
     *
     * @param errorData 错误数据
     *
     * @return {@link BaseException}
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseException> T withData(Object errorData) {
        this.errorData = errorData;
        return (T) this;
    }
}
