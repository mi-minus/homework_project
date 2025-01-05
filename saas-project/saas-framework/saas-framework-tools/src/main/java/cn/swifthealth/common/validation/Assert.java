package cn.swifthealth.common.validation;

import cn.swifthealth.common.function.Function3;
import cn.swifthealth.common.tools.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * 断言,该工具类对结果进行期望表达式判断，不满足期望值，将抛出指定异常
 *
 * @author zhouyuzhou
 */
public class Assert {

    /**
     * 断言给定的表达式的布尔值为真
     *
     * @param expression        表达式
     * @param exceptionSupplier 异常供应商
     *
     * @throws X x
     */
    public static <X extends Throwable> void isTrue(boolean expression, final Supplier<X> exceptionSupplier) throws X {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 断言给定的表达式的布尔值为假
     *
     * @param expression 表达式
     * @param exception  异常
     *
     * @throws X x
     */
    public static <X extends Throwable> void isFalse(boolean expression, final Supplier<X> exception) throws X {
        if (expression) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的对象为空
     *
     * @param object    对象
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void isNull(T object, final Supplier<X> exception) throws X {
        // java.util.Optional类型
        if (object instanceof Optional && ((Optional<?>) object).isPresent()) {
            throw exception.get();
        }

        if (null != object) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的集合为空
     *
     * @param collection 集合
     * @param exception  异常
     *
     * @throws X x
     */
    public static <T extends Collection<?>, X extends Throwable> void isNull(T collection, final Supplier<X> exception) throws X {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的对象不为空
     *
     * @param obj       对象
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void isNotNull(T obj, final Supplier<X> exception) throws X {
        if (null == obj) {
            throw exception.get();
        }

        // java.util.Optional类型
        if (obj instanceof Optional && !((Optional<?>) obj).isPresent()) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的集合不为空
     *
     * @param collection 集合
     * @param exception  异常
     *
     * @throws X x
     */
    public static <T extends Collection<?>, X extends Throwable> void isNotNull(T collection, final Supplier<X> exception) throws X {
        if (CollectionUtil.isEmpty(collection)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的对象为空<br/>
     * 1. {@link CharSequence}<br/>
     * 2. {@link java.util.Map}<br/>
     * 3. {@link Iterable}<br/>
     * 4. {@link java.util.Iterator}<br/>
     * 5. {@link java.lang.reflect.Array}<br/>
     *
     * @param obj       数组
     * @param exception 异常
     *
     * @throws X x
     * @see ObjectUtil#isNotEmpty(Object)
     */
    public static <T, X extends Throwable> void isEmpty(T obj, final Supplier<X> exception) throws X {
        if (ObjectUtil.isNotEmpty(obj)) {
            throw exception.get();
        }
    }

    /**
     * 断言是否为空
     *
     * @param obj       obj
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void isEmpty(T obj, final Function<T, X> exception) throws X {
        if (ObjectUtil.isNotEmpty(obj)) {
            throw exception.apply(obj);
        }
    }

    /**
     * 断言给定的字符串为空
     *
     * @param string    字符串
     * @param exception 异常
     *
     * @throws X x
     */
    public static <X extends Throwable> void isEmpty(CharSequence string, final Supplier<X> exception) throws X {
        if (StringUtil.isNotEmpty(string)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的数组不为空
     *
     * @param obj       数组
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void isNotEmpty(T obj, final Supplier<X> exception) throws X {
        if (ObjectUtil.isEmpty(obj)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的字符串不为空
     *
     * @param string    字符串
     * @param exception 异常
     *
     * @throws X x
     */
    public static <X extends Throwable> void isNotEmpty(CharSequence string, final Supplier<X> exception) throws X {
        if (StringUtil.isEmpty(string)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的字符串为空
     *
     * @param string    字符串
     * @param exception 异常
     *
     * @throws X x
     */
    public static <X extends Throwable> void isBlank(CharSequence string, final Supplier<X> exception) throws X {
        if (StringUtil.isNotBlank(string)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的字符串不为空
     *
     * @param string    字符串
     * @param exception 异常
     *
     * @throws X x
     */
    public static <X extends Throwable> void isNotBlank(CharSequence string, final Supplier<X> exception) throws X {
        if (StringUtil.isBlank(string)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的对象相等
     *
     * @param o1        object1
     * @param o2        object2
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void isEquals(T o1, T o2, final Supplier<X> exception) throws X {
        if (!ObjectUtil.equals(o1, o2)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的对象数组相等
     *
     * @param object1   中object1
     * @param object2   object2
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void isEquals(T[] object1, T[] object2, final Supplier<X> exception) throws X {
        if (!Arrays.equals(object1, object2)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的对象不相等
     *
     * @param exception 异常
     * @param o1        o1群
     * @param o2        o2
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void unEquals(T o1, T o2, final Supplier<X> exception) throws X {
        if (ObjectUtil.equals(o1, o2)) {
            throw exception.get();
        }
    }

    /**
     * 断言给定的对象数组不相等
     *
     * @param object1   中object1
     * @param object2   object2
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void unEquals(T[] object1, T[] object2, final Supplier<X> exception) throws X {
        if (Arrays.equals(object1, object2)) {
            throw exception.get();
        }
    }

    /**
     * 长度等于
     *
     * @param obj       obj
     * @param length    长度
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void length(T obj, int length, final Supplier<X> exception) throws X {
        if (ObjectUtil.length(obj) != length) {
            throw exception.get();
        }
    }

    /**
     * 长度大于
     *
     * @param obj       obj
     * @param length    长度
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T, X extends Throwable> void lengthGreater(T obj, int length, final Supplier<X> exception) throws X {
        if (ObjectUtil.length(obj) <= length) {
            throw exception.get();
        }
    }

    /**
     * 数字小于等于{@code limit}或为null
     *
     * @param obj       obj
     * @param limit     限制
     * @param exception 异常
     *
     * @throws X x
     */
    public static <T extends Number, X extends Throwable> void numberLessEqualOrNull(T obj, T limit, final Supplier<X> exception) throws X {
        if (null == obj) {
            return;
        }

        if (NumberUtil.isGreaterOrAnyNull(obj, limit)) {
            throw exception.get();
        }
    }

    /**
     * 校验{@code number}是否在{@code min}和{@code max}之间，等价于{@code mix < number < max}
     *
     * @param number    实际校验的数
     * @param min       最小值，不包含
     * @param max       最大值，不包含
     * @param exception 异常，泛型为{@code number, min, max}
     *
     * @throws X x
     */
    public static <T extends Number, X extends Throwable> void numberRange(T number, T min, T max, final Function3<T, T, T, X> exception) throws X {
        if (NumberUtil.isGreater(number, min)) {
            return;
        }

        if (NumberUtil.isLess(number, max)) {
            return;
        }

        throw exception.apply(number, min, max);
    }

    /**
     * 断言{@code obj}是{@code clazz}的实例
     *
     * @param obj       对象
     * @param clazz     clazz
     * @param exception 例外
     *
     * @throws X x
     */
    public static <X extends Throwable> void isInstance(Object obj, Class<?> clazz, final Supplier<X> exception) throws X {
        isTrue(clazz.isInstance(obj), exception);
    }

    /**
     * 断言{@code obj}是{@code null}或{@code clazz}的实例
     *
     * @param obj       对象
     * @param clazz     clazz
     * @param exception 例外
     *
     * @throws X x
     */
    public static <X extends Throwable> void isNullOrInstance(Object obj, Class<?> clazz, final Supplier<X> exception) throws X {
        isTrue(null == obj || clazz.isInstance(obj), exception);
    }

    /**
     * 断言,仅有一个元素
     *
     * @param c         c
     * @param exception 例外
     *
     * @throws X x
     */
    public static <X extends Throwable> void onlyOne(Collection<?> c, final Supplier<X> exception) throws X {
        if (CollectionUtil.hasOnlyOne(c)) {
            return;
        }

        throw exception.get();
    }

    /**
     * 命中数据库行
     *
     * @param row       一行
     * @param exception 例外
     *
     * @throws X x
     */
    public static <X extends Throwable> void hitDbRow(Number row, final Supplier<X> exception) throws X {
        if (DbUtil.existLine(row)) {
            return;
        }

        throw exception.get();
    }

    /**
     * 未命中数据库行
     *
     * @param row       行
     * @param exception 例外
     *
     * @throws X x
     */
    public static <X extends Throwable> void unHitDbRow(Number row, final Supplier<X> exception) throws X {
        if (DbUtil.unExistLine(row)) {
            return;
        }

        throw exception.get();
    }
}
