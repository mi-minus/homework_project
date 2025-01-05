package cn.swifthealth.common.tools;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p> 布尔工具类 </p>
 *
 * @author <a href="mailto:minus@outlook.com">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class BooleanUtil extends cn.hutool.core.util.BooleanUtil {
    /**
     * 大写字母{@code Y}
     */
    public static final String UPPER_CASE_Y = "Y";

    /**
     * 大写字母{@code N}
     */
    public static final String UPPER_CASE_N = "N";

    /**
     * 值
     *
     * @param value     值
     * @param nullValue null值
     *
     * @return boolean
     */
    public boolean value(Boolean value, boolean nullValue) {
        return null == value ? nullValue : value;
    }

    /**
     * 断言
     *
     * @param obj         obj
     * @param returnsNull 返回null
     * @param predicates  谓词
     * @param combination 结合
     *
     * @return boolean
     */
    @SafeVarargs
    public static <T> boolean assertion(final T obj, final boolean returnsNull,
                                        final Function<Boolean[], Boolean> combination,
                                        final Predicate<T>... predicates) {
        if (null == obj) {
            return returnsNull;
        }
        return combination.apply(Arrays.stream(predicates).map(s -> s.test(obj)).toArray(Boolean[]::new));
    }

    /**
     * 断言且
     *
     * @param obj         obj
     * @param returnsNull 返回null
     * @param predicates  谓词
     *
     * @return boolean
     */
    @SafeVarargs
    public static <T> boolean assertAnd(final T obj, final boolean returnsNull, final Predicate<T>... predicates) {
        if (null == obj) {
            return returnsNull;
        }
        return assertion(obj, returnsNull, BooleanUtil::andOfWrap, predicates);
    }

    /**
     * 断言或
     *
     * @param obj         obj
     * @param returnsNull 返回null
     * @param predicates  谓词
     *
     * @return boolean
     */
    @SafeVarargs
    public static <T> boolean assertOr(final T obj, final boolean returnsNull, final Predicate<T>... predicates) {
        if (null == obj) {
            return returnsNull;
        }
        return assertion(obj, returnsNull, BooleanUtil::orOfWrap, predicates);
    }

    /**
     * 是{@code null}或{@code false}
     *
     * @param value 价值
     *
     * @return boolean
     */
    public static boolean isNullOrFalse(final Boolean value) {
        return null == value || !value;
    }

    /**
     * 为{@code null}或{@code true}
     *
     * @param value 值
     *
     * @return boolean
     */
    public static boolean isNullOrTrue(final Boolean value) {
        return null == value || value;
    }

    /**
     * 拆箱,从包装类{@link Boolean}转为基本类型{@code boolean},防止空指针，{@code null}为{@code false}
     *
     * @param value 价值
     *
     * @return boolean
     */
    public static boolean unboxing(final Boolean value) {
        return null != value && value;
    }

    /**
     * 转为字符串，空时，交给调用方处理
     *
     * @param value       价值
     * @param trueString  真正字符串
     * @param falseString 错误字符串
     *
     * @return {@link Optional}<{@link String}>
     */
    public static Optional<String> toNullableString(Boolean value, String trueString, String falseString) {
        return Optional.ofNullable(toString(value, trueString, falseString));
    }

    /**
     * 可以为空字符串大写y
     *
     * @param value 价值
     *
     * @return {@link String}
     */
    public static Optional<String> toStringUpperCaseYs(Boolean value) {
        return toNullableString(value, UPPER_CASE_Y, UPPER_CASE_N);
    }

    /**
     * 可以为空字符串大写y
     *
     * @param value 价值
     *
     * @return {@link String}
     */
    public static String toNullableStringUpperCaseYs(Boolean value) {
        return toStringUpperCaseYs(value).orElse(null);
    }

    /**
     * 转为{@code boolean}
     *
     * @param value        价值
     * @param trueValues   真实值
     * @param falseValues  错误价值观
     * @param defaultValue 默认值
     *
     * @return boolean
     */
    public static <T> boolean toBoolean(T value, T[] trueValues, T[] falseValues, boolean defaultValue) {
        if (null == value) {
            return defaultValue;
        }

        if (ArrayUtil.contains(trueValues, value)) {
            return true;
        }

        if (ArrayUtil.contains(falseValues, value)) {
            return false;
        }

        return defaultValue;
    }


    /**
     * 转为{@code boolean},默认{@code false}
     *
     * @param value      价值
     * @param trueValues 真实值
     *
     * @return boolean
     */
    @SafeVarargs
    public static <T> boolean toBoolean(T value, T... trueValues) {
        return toBoolean(value, trueValues, null, false);
    }


    /**
     * 转为布尔值
     *
     * @param o o
     *
     * @return {@link Boolean}
     */
    public static Boolean toBoolean(Object o) {
        if (null == o) {
            return null;
        }

        if (o instanceof Boolean) {
            return (Boolean) o;
        }

        return toBoolean(o.toString());
    }

    /**
     * 扁平转换
     *
     * @param value    boolean值
     * @param trueVal  为真时的值
     * @param falseVal 为假时的值
     * @param nullVal  为null时的值
     *
     * @return {@link T}
     */
    public static <T> T flatMap(Boolean value, T trueVal, T falseVal, T nullVal) {
        if (null == value) {
            return nullVal;
        }
        return value ? trueVal : falseVal;
    }
}
