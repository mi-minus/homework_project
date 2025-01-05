package cn.swifthealth.common.function;

/**
 * <p> 函数式接口3个参数 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 2.23.0
 */
@FunctionalInterface
public interface Function3<P1, P2, P3, R> {
    R apply(P1 p1, P2 p2, P3 p3);
}
