package cn.swifthealth.common.tools;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * <p> 排序工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
public class CompareUtil extends cn.hutool.core.comparator.CompareUtil {


    /**
     * 排序
     *
     * @param list 列表
     * @param c    c
     *
     * @return {@link List}<{@link T}>
     */
    public static <T, U extends Comparable<? super U>> List<T> sort(final List<T> list, Function<T, ? extends U> c) {
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }

        list.sort(Comparator.comparing(c));

        return list;
    }


    /**
     * 递归比较字段，直至分出先后，或两者完全相等
     *
     * @param o1            o1
     * @param o2            o2
     * @param isNullGreater 大空
     * @param fields        字段
     *
     * @return int
     */
    @SafeVarargs
    public static <T> int thenCompare(T o1, T o2, boolean isNullGreater, Function<T, ?>... fields) {
        if (o1 == o2) {
            return 0;
        }

        if (null == o1) {
            // null 排在后面
            return isNullGreater ? 1 : -1;
        }

        if (null == o2) {
            return isNullGreater ? -1 : 1;
        }

        // 比较字段
        for (Function<T, ?> field : fields) {
            final Object fieldOfO1 = field.apply(o1);
            final Object fieldOfO2 = field.apply(o2);

            // 字段比较
            final int fieldEquals = compare(fieldOfO1, fieldOfO2, isNullGreater);

            // 已经分出先后，直接返回
            if (0 != fieldEquals) {
                return fieldEquals;
            }
        }

        // 尚未分出先后，说明相等
        return 0;
    }

    /**
     * 比较,更加精细化的比较
     *
     * @param o1          o1群
     * @param o2          o2
     * @param compareInfo 比较信息
     *
     * @return int
     */
    public static <T> int thenCompare(T o1, T o2, CompareInfo<T> compareInfo) {
        if (null == compareInfo) {
            return compare(o1, o2, false);
        }

        // 递归比较结果
        final Integer recursionCompare = compareInfo.compareTo(o1, o2);

        // 尚未分出先后，说明相等
        return null != recursionCompare ? recursionCompare : compare(o1, o2, compareInfo.isNullGreater());
    }


    /**
     * 通过{@link Comparable#compareTo(Object)}方法来比较是否相等
     *
     * @param c1 c1
     * @param c2 c2
     *
     * @return boolean
     */
    public static boolean equals(Object c1, Object c2) {
        return compare(c1, c2, false) == 0;
    }


    /**
     * 比较大小，参数1 &gt; 参数2 返回true
     *
     * @param a 元素a
     * @param b 元素b
     *
     * @return 是否大于
     */
    public static <T extends Comparable<? super T>> boolean isGreater(T a, T b) {
        return a.compareTo(b) > 0;
    }

    /**
     * 比较大小，参数1 &gt;= 参数2 返回true
     *
     * @param a 元素a
     * @param b 元素b
     *
     * @return 是否大于等于
     */
    public static <T extends Comparable<? super T>> boolean isGreaterOrEqual(T a, T b) {
        return a.compareTo(b) >= 0;
    }

    /**
     * 比较大小，参数1 &lt; 参数2 返回true
     *
     * @param a 元素a
     * @param b 元素b
     *
     * @return 是否小于
     */
    public static <T extends Comparable<? super T>> boolean isLess(T a, T b) {
        return a.compareTo(b) < 0;
    }

    /**
     * 比较大小，参数1&lt;=参数2 返回true
     *
     * @param a 元素a
     * @param b 元素b
     *
     * @return 是否小于等于
     */
    public static <T extends Comparable<? super T>> boolean isLessOrEqual(T a, T b) {
        return a.compareTo(b) <= 0;
    }

    /**
     * 根据{@code compare}获得两个中较小的一个
     *
     * @param a       a
     * @param b       b
     * @param compare 比较
     *
     * @return {@link T}
     */
    public static <T> T min(final T a, T b, Comparator<T> compare) {
        if (compare.compare(a, b) <= 0) {
            return a;
        }
        return b;
    }

    /**
     * 最大值
     *
     * @param a         a
     * @param b         b
     * @param isNullMax null值是否最大
     *
     * @return {@link T}
     */
    public static <T extends Comparable<? super T>> T max(final T a, T b, boolean isNullMax) {
        if (null == a || null == b) {
            return isNullMax ? null : null == a ? b : a;
        }
        return isLess(a, b) ? b : a;
    }
}
