package cn.swifthealth.common.except;

/**
 * <p> 业务异常 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 2.21.2
 */

public class BusinessException extends BaseException {
    private static final long serialVersionUID = -3424229214596286788L;

    public BusinessException(int messageSourceId) {
        super(messageSourceId);
    }

    public BusinessException(int messageSourceId, final Object... args) {
        super(messageSourceId, args);
    }


    /**
     * 业务异常
     *
     * @param message 消息
     *
     * @deprecated 请根据异常规范使用 {@link #BusinessException(int)}
     */
    @Deprecated
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 通过状态码查找Resource Bundle文件作为Message返回，携带异常参数
     *
     * @param code 状态码
     * @param args 异常参数
     *
     * @return {@link BusinessException}
     */
    public static BusinessException with(int code, Object... args) {
        return new BusinessException(code, args);
    }

    /**
     * 通过错误数据和参数构造异常
     *
     * @param code      代码
     * @param errorData 错误数据
     * @param args      arg游戏
     *
     * @return {@link BusinessException}
     */
    public static BusinessException withData(int code, Object errorData, Object... args) {
        return new BusinessException(code, args).withData(errorData);
    }
}
