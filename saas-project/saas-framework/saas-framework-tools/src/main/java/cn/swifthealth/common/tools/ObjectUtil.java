package cn.swifthealth.common.tools;

import cn.hutool.core.util.CharUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p> 对象工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
public class ObjectUtil extends cn.hutool.core.util.ObjectUtil {

    /**
     * 实际意义上的空对象，没有内容
     *
     * @param arg 参数
     *
     * @return boolean
     */
    public static <T> boolean isBlank(final T arg) {
        if (isNull(arg)) {
            return true;
        }

        if (arg instanceof Collection) {
            return CollectionUtil.isEmpty((Collection<?>) arg);
        }

        if (ArrayUtil.isArray(arg)) {
            return ArrayUtil.isEmpty(arg);
        }

        if (arg instanceof Map) {
            return CollectionUtil.isEmpty((Map<?, ?>) arg);
        }

        if (arg instanceof Iterable) {
            return CollectionUtil.isEmpty((Iterable<?>) arg);
        }

        if (arg instanceof Iterator) {
            return CollectionUtil.isEmpty((Iterator<?>) arg);
        }

        if (arg instanceof CharSequence) {
            return StringUtil.isBlank((CharSequence) arg);
        }

        if (arg instanceof Enumeration) {
            return ((Enumeration<?>) arg).hasMoreElements();
        }

        if (arg instanceof Optional<?>) {
            return !((Optional<?>) arg).isPresent();
        }

        return false;
    }

    /**
     * 非实际意义上的空白对象
     *
     * @param arg 参数
     *
     * @return boolean
     */
    public static <T> boolean isNotBlank(final T arg) {
        if (isNull(arg)) {
            return false;
        }

        if (arg instanceof Collection) {
            return CollectionUtil.isNotEmpty((Collection<?>) arg);
        }

        if (ArrayUtil.isArray(arg)) {
            return ArrayUtil.isNotEmpty(arg);
        }

        if (arg instanceof Map) {
            return CollectionUtil.isNotEmpty((Map<?, ?>) arg);
        }

        if (arg instanceof Iterable) {
            return CollectionUtil.isNotEmpty((Iterable<?>) arg);
        }

        if (arg instanceof Iterator) {
            return CollectionUtil.isNotEmpty((Iterator<?>) arg);
        }

        if (arg instanceof CharSequence) {
            return StringUtil.isNotBlank((CharSequence) arg);
        }

        return true;
    }


    /**
     * 否则
     *
     * @param obj             obj
     * @param returnPredicate 返回谓词
     * @param other           其他
     *
     * @return {@link T}
     */
    public static <T> T orElse(final T obj, final Predicate<T> returnPredicate, final T other) {
        return returnPredicate.test(obj) ? obj : other;
    }

    /**
     * 类似 {@code obj instanceof instanceClass ? obj : other}
     *
     * @param obj           对象
     * @param instanceClass 实例类
     * @param other         另外
     *
     * @return {@link T}
     */
    @SuppressWarnings("unchecked")
    public static <T> T orElse(final Object obj, final Class<?> instanceClass, final Supplier<T> other) {
        return instanceClass.isInstance(obj) ? (T) obj : other.get();
    }

    /**
     * 否则
     *
     * @param obj   obj
     * @param other 其他
     *
     * @return {@link T}
     */
    public static <T> T orElse(final T obj, final T other) {
        return orElse(obj, Objects::nonNull, other);
    }

    /**
     * 否则
     *
     * @param obj             obj
     * @param returnPredicate 返回谓词
     * @param other           其他
     *
     * @return {@link T}
     */
    public static <T> T orElse(final T obj, final Predicate<T> returnPredicate, final Supplier<T> other) {
        return returnPredicate.test(obj) ? obj : other.get();
    }

    /**
     * 否则
     *
     * @param obj   obj
     * @param other 其他
     *
     * @return {@link T}
     */
    public static <T> T orElse(final T obj, final Supplier<T> other) {
        return orElse(obj, Objects::nonNull, other.get());
    }

    /**
     * 如果{@code obj}符合{@code returnPredicate},则使用{@code func}将其转换，否则返回{@code other}
     *
     * @param obj             obj
     * @param returnPredicate 返回谓词
     * @param func            函数
     * @param other           其他
     *
     * @return {@link R}
     */
    public static <T, R> R orElseMap(final T obj, final Predicate<T> returnPredicate, Function<T, R> func, final Supplier<R> other) {
        return returnPredicate.test(obj) ? func.apply(obj) : other.get();
    }

    /**
     * 映射
     *
     * @param obj     obj
     * @param mapFunc 映射函数
     *
     * @return {@link R}
     */
    public static <T, R> R map(final T obj, Function<T, R> mapFunc) {
        return null != obj ? mapFunc.apply(obj) : null;
    }

    /**
     * 地图
     *
     * @param obj      obj
     * @param mapFunc1 地图func1
     * @param mapFunc  映射函数
     *
     * @return {@link R1}
     */
    public static <T, R, R1> R1 map(final T obj, Function<T, R> mapFunc1, Function<R, R1> mapFunc) {
        return map(map(obj, mapFunc1), mapFunc);
    }

    /**
     * 类型强制转换，抑制警告
     *
     * @param obj obj
     *
     * @return {@link T}
     *
     * @throws ClassCastException 类转换异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object obj) throws ClassCastException {
        return (T) obj;
    }

    /**
     * 类型强制转换
     *
     * @param obj        obj
     * @param exSupplier 转换异常后的处理异常
     *
     * @return {@link T}
     *
     * @throws X x
     */
    @SuppressWarnings("unchecked")
    public static <T, X extends Throwable> T cast(final Object obj, final Supplier<X> exSupplier) throws X {
        try {
            return (T) obj;
        } catch (ClassCastException e) {
            throw exSupplier.get();
        }
    }

    /**
     * 类型强制转换，提供手动转换选项
     *
     * @param obj      obj
     * @param clazz    clazz
     * @param castFunc 转换函数
     *
     * @return {@link T}
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object obj, Class<T> clazz, Function<Object, T> castFunc) {
        return clazz.isInstance(obj) ? (T) obj : castFunc.apply(obj);
    }

    /**
     * 合并,返回第一个被{@link Predicate}命中的值
     *
     * @param predicate 谓词
     * @param args      arg游戏
     *
     * @return {@link T}
     */
    @SafeVarargs
    public static <T> T coalesce(final Predicate<T> predicate, final T... args) {
        if (ArrayUtil.isEmpty(args)) {
            return null;
        }

        for (T arg : args) {
            if (predicate.test(arg)) {
                return arg;
            }
        }

        return null;
    }

    /**
     * 懒惰合并,返回第一个被{@link Predicate}命中的值
     *
     * @param predicate    谓词
     * @param argSuppliers arg供应商
     *
     * @return {@link T}
     */
    @SafeVarargs
    public static <T> T lazyCoalesce(final Predicate<T> predicate, final Supplier<T>... argSuppliers) {
        if (ArrayUtil.isEmpty(argSuppliers)) {
            return null;
        }

        for (Supplier<T> argSupplier : argSuppliers) {
            T arg = argSupplier.get();
            if (predicate.test(arg)) {
                return arg;
            }
        }

        return null;
    }

    /**
     * 两个对象比较相等，通常使用{@link cn.hutool.core.util.ObjectUtil#equals(Object, Object)}来比较，
     * 但是该方法要求两个参数的类型必须全部相等，该方法将忽略实际类型，比较值相等
     *
     * @param a 一个
     * @param b b
     *
     * @return boolean
     */
    public static boolean equals(final Object a, final Object b) {
        // number
        if (a instanceof Number && b instanceof Number) {
            return NumberUtil.equals((Number) a, (Number) b);
        }

        // array
        if (a instanceof Object[] && b instanceof Object[]) {
            return Arrays.equals((Object[]) a, (Object[]) b);
        }

        // any String
        if (a instanceof CharSequence || b instanceof CharSequence) {
            return Objects.equals(StringUtil.toString(a), StringUtil.toString(b));
        }

        return Objects.equals(a, b);
    }

    /**
     * 比较对象是否相等,可针对性忽略{@code input}为{@code null}的结果，指定{@code inputNullResult}
     *
     * @param input           输入
     * @param target          目标
     * @param inputNullResult 输入null结果
     *
     * @return boolean
     */
    public static boolean equals(final Object input, final Object target, boolean inputNullResult) {
        if (null == input) {
            return inputNullResult;
        }

        return equals(input, target);
    }

    /**
     * 任意对象转字符串
     *
     * @param obj obj
     *
     * @return {@link String}
     */
    public static String toString(final Object obj) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof CharSequence) {
            return obj.toString();
        }

        if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.toString(obj);
        }

        if (CharUtil.isChar(obj)) {
            return CharUtil.toString((char) obj);
        }

        return obj.toString();
    }

    /**
     * 链式访问自身
     *
     * @return {@link T}
     */
    public static <T> T accessorChain(T obj, Consumer<T> consumer) {
        consumer.accept(obj);
        return obj;
    }

    /**
     * 断言消费
     *
     * @param obj        obj
     * @param assertFunc 断言的取值函数
     * @param predicate  谓词
     * @param consumer   消费者
     */
    public static <T, R> void assertConsumer(T obj, Function<T, R> assertFunc, Predicate<R> predicate, Consumer<T> consumer) {
        R assertValue = assertFunc.apply(obj);
        if (predicate.test(assertValue)) {
            return;
        }
        consumer.accept(obj);
    }

    /**
     * 断言到null则执行消费
     *
     * @param obj        obj
     * @param assertFunc 断言的取值函数
     * @param consumer   消费者
     */
    public static <T, R> void assertNullConsumer(T obj, Function<T, R> assertFunc, Consumer<T> consumer) {
        assertConsumer(obj, assertFunc, Objects::nonNull, consumer);
    }

    /**
     * Construct a tuple of degree 3.
     */
    public static <T1, T2, T3> Tuple3<T1, T2, T3> tuple(T1 v1, T2 v2, T3 v3) {
        return new Tuple3<>(v1, v2, v3);
    }

    /**
     * 对一个对象中的引用类型字段，先调用{@code getter},如果不为{@code null}直接返回，否则，使用{@code initializer}初始化后，通过{@code setter}赋值,然后返回，
     * 例如以下代码
     * <pre>{@code
     * class Demo {
     *     private List<String> list;
     *
     *     private Map<String, Integer> map;
     *
     *     // 省略getter setter
     *
     *     public void demo() {
     *         Demo d = new Demo();
     *         d.setList(List.of("test"));
     *
     *         List<String> l = d.getList();
     *         if (null == l) {
     *             l = new ArrayList<>();
     *             d.setList(l);
     *         }
     *
     *         // 针对l的其他操作
     *
     *         Map<String, Integer> m = d.getMap();
     *         if (null == m) {
     *             m = new HashMap<>();
     *             d.setMap(m);
     *         }
     *
     *         // 针对m的其他操作
     *
     *     }
     * }
     * }</pre>等价于
     * <pre>{@code
     * class Demo {
     *     private List<String> list;
     *
     *     private Map<String, Integer> map;
     *
     *     // 省略getter setter
     *
     *     public void demo() {
     *         Demo d = new Demo();
     *         d.setList(List.of("test"));
     *
     *         // 1.list
     *         List<String> a = ObjectUtil.getOrSetField(d::getList, ArrayList::new, d::setList);
     *
     *         // 针对l的其他操作
     *
     *         // 2.map
     *         Map<String, Integer> b = ObjectUtil.getOrSetField(d::getMap, HashMap::new, d::setMap);
     *
     *         // 针对m的其他操作
     *
     *     }
     * }
     * }</pre>
     *
     * @param getter          get方法
     * @param getterValueTest getter方法返回值断言
     * @param initializer     初始化器
     * @param setter          set方法
     *
     * @return {@link R}
     */
    public static <R> R getOrSetField(Supplier<R> getter, Predicate<R> getterValueTest, Supplier<R> initializer, Consumer<R> setter) {
        R fieldObject = getter.get();
        if (getterValueTest.test(fieldObject)) {
            return fieldObject;
        }
        fieldObject = initializer.get();
        setter.accept(fieldObject);
        return fieldObject;
    }

    /**
     * 获取或为空时，先设置字段再获取
     *
     * @param getter      get方法
     * @param initializer 初始化器
     * @param setter      set方法
     *
     * @return {@link R}
     *
     * @see #getOrSetField(Supplier, Predicate, Supplier, Consumer)
     */
    public static <R> R getOrSetField(Supplier<R> getter, Supplier<R> initializer, Consumer<R> setter) {
        return getOrSetField(getter, Objects::nonNull, initializer, setter);
    }

    /**
     * 如果是一个空对象，则转为{@code null}
     *
     * @param value 值
     *
     * @return {@link V}
     */
    @SuppressWarnings("unchecked")
    public static <V> V nullIfBlank(V value) {
        if (null == value) {
            return null;
        }

        if (value instanceof CharSequence) {
            return (V) StringUtil.blankToNull((CharSequence) value);
        }

        if (value instanceof List<?>) {
            return (V) CollectionUtil.nullIfEmpty((List<?>) value);
        }

        if (value instanceof Set<?>) {
            return (V) CollectionUtil.nullIfEmpty((Set<?>) value);
        }

        // 等等
        return value;
    }

    /**
     * 如果{@code value}为{@code null},则通过{@code emptySupplier}初始化并返回一个空的对象
     *
     * @param value         值
     * @param emptySupplier 空供应商
     *
     * @return {@link V}
     */
    public static <V> V emptyIfNull(V value, Supplier<V> emptySupplier) {
        return null == value ? emptySupplier.get() : value;
    }
}
