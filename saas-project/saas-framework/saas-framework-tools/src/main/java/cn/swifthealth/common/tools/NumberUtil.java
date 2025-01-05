package cn.swifthealth.common.tools;

import cn.hutool.core.lang.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Copyright (C) @2022 minus@swifthealth.cn
 * <p>文件名称: NumberUtil </p>
 * <p>描述: 注意，该类在3.0上需要合并 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 */
public class NumberUtil extends cn.hutool.core.util.NumberUtil {

    /**
     * 转换为{@link Long}类型
     *
     * @param obj obj
     *
     * @return {@link Long}
     *
     * @throws NumberFormatException 数字格式异常
     */
    public static Long toLong(final Object obj) throws NumberFormatException {
        if (null == obj) {
            return null;
        }

        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }

        if (obj instanceof String) {
            return Long.parseLong((String) obj);
        }

        return toLong(obj.toString());
    }

    /**
     * 转为Long类型
     *
     * @param obj obj
     *
     * @return {@link Long}
     *
     * @throws NumberFormatException 数字格式异常
     */
    public static long toLong(final Object obj, final long other) throws NumberFormatException {
        if (null == obj) {
            return other;
        }

        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }

        if (obj instanceof String) {
            return Long.parseLong((String) obj);
        }

        return toLong(obj.toString());
    }

    /**
     * 安全的转为{@link Long}类型
     *
     * @param obj obj
     *
     * @return {@link Long}
     */
    public static Long safeToLong(final Object obj) {
        try {
            return toLong(obj);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 安全的转为{@link Long}类型
     *
     * @param other 其他
     * @param obj   obj
     *
     * @return {@link Long}
     */
    public static long safeToLong(final Object obj, final long other) {
        try {
            return toLong(obj, other);
        } catch (NumberFormatException e) {
            return other;
        }
    }

    /**
     * 转为基本类型的Long类型，默认值为{@code 0}
     *
     * @param o o
     *
     * @return long
     */
    public static long toPrimitiveLong(Object o) {
        return ObjectUtil.orElse(toLong(o), 0L);
    }

    /**
     * 转为{@link Integer}类型
     *
     * @param obj obj
     *
     * @return {@link Integer}
     *
     * @throws NumberFormatException 数字格式异常
     */
    public static Integer toInteger(final Object obj) throws NumberFormatException {
        if (null == obj) {
            return null;
        }

        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }

        if (obj instanceof String) {
            return Integer.parseInt((String) obj);
        }

        return toInteger(obj.toString());
    }

    /**
     * 转为基本类型的int
     *
     * @param o o
     *
     * @return int
     */
    public static int toInt(Object o) {
        return ObjectUtil.orElse(toInteger(o), 0);
    }

    /**
     * 转为基本类型的int
     *
     * @param o o
     *
     * @return int
     */
    public static int toInt(Object o, int other) {
        return ObjectUtil.orElse(toInteger(o), other);
    }

    /**
     * 安全的转为Integer类型
     *
     * @param obj obj
     *
     * @return {@link Integer}
     */
    public static Integer safeToInteger(final Object obj) {
        try {
            return toInteger(obj);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    /**
     * 比较两个数字是否相等，尽可能使用{@code ==}比较，该方法主要避免了两个数的实际类型不同时的比较问题
     *
     * @param a 一个
     * @param b b
     *
     * @return boolean
     */
    public static boolean equals(final Number a, Number b) {
        if (null == a || null == b) {
            return false;
        }

        // long
        if (a instanceof Long && b instanceof Long) {
            return equals(a.longValue(), b.longValue());
        }

        // int
        if (a instanceof Integer && b instanceof Integer) {
            return equals(a.intValue(), b.intValue());
        }

        if (a instanceof BigDecimal && b instanceof BigDecimal) {
            return equals((BigDecimal) a, (BigDecimal) b);
        }

        return equals(toBigDecimal(a), toBigDecimal(b));
    }

    /**
     * 使用{@link Comparable}比较是否相等
     *
     * @param a 一
     * @param b b
     *
     * @return boolean
     */
    public static boolean isEqual(Number a, Number b) {
        // noinspection NumberEquality
        if (a == b) {
            // 如果用户传入同一对象，省略compareTo以提高性能。
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return CompareUtil.equals(a, b);
    }

    /**
     * 将人民币由元转为分
     *
     * @param rmbYuan 元人民币
     *
     * @return int
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static int rmbYuanToCent(BigDecimal rmbYuan) {
        if (null == rmbYuan) {
            return BigDecimal.ZERO.intValue();
        }
        return rmbYuan.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 大于或至少有一个为空
     *
     * @param a 一个
     * @param b b
     *
     * @return boolean
     */
    public static boolean isGreaterOrAnyNull(Number a, Number b) {
        return isGreater(toBigDecimal(a), toBigDecimal(b));
    }

    /**
     * 比较大小，参数1 &gt; 参数2 返回true
     *
     * @param a 数字1
     * @param b 数字2
     *
     * @return 是否大于
     *
     * @since 3.0.0
     */
    public static boolean isGreater(Number a, Number b) {
        Assert.notNull(a);
        Assert.notNull(b);
        return isGreater(toBigDecimal(a), toBigDecimal(b));
    }

    /**
     * 比较大小，参数1 &lt; 参数2 返回true
     *
     * @param a 数字1
     * @param b 数字2
     *
     * @return 是否小于
     *
     * @since 3.0.0
     */
    public static boolean isLess(Number a, Number b) {
        Assert.notNull(a);
        Assert.notNull(b);
        return isLess(toBigDecimal(a), toBigDecimal(b));
    }

    /**
     * 按比例分配，基本计算规则{@code percentage / 100 * total}
     *
     * @param percentage 百分比，例如{@code 30%},这里应该输入{@code 30}
     * @param total      总,要分配的总量
     *
     * @return {@link BigDecimal}
     */
    public static BigDecimal percentage(BigDecimal percentage, BigDecimal total) {
        final BigDecimal proportional = div(percentage, toBigDecimal(100));
        return mul(proportional, total);
    }

    /**
     * 提供精确的加法运算<br>,可任意为{@code null}或{@code 0},结果不变
     *
     * @param v1 被加数
     * @param v2 加数
     *
     * @return 和
     */
    public static long add(Long v1, Long v2) {
        return add(new Number[] { v1, v2 }).longValue();
    }

    /**
     * 金钱合理化,对 <= 0和null值处理为0
     *
     * @return {@link BigDecimal}
     */
    public static BigDecimal moneyRationalize(BigDecimal value) {
        if (null == value) {
            return BigDecimal.ZERO;
        }

        return isLess(value, BigDecimal.ZERO) ? BigDecimal.ZERO : value;
    }
}
