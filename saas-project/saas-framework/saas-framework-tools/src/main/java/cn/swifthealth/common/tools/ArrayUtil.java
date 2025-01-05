package cn.swifthealth.common.tools;

import cn.hutool.core.io.FastByteBuffer;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * <p> 数组工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ArrayUtil extends cn.hutool.core.util.ArrayUtil {

    /**
     * 空字节数组
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * 创建数组,可变长参数，方便传参
     *
     * @param array 数组
     *
     * @return {@code T[]}
     */
    @SafeVarargs
    public static <T> T[] newArray(T... array) {
        return array;
    }

    /**
     * 转为Stream
     *
     * @param array 数组
     *
     * @return {@link Stream}<{@link T}>
     */
    @SafeVarargs
    public static <T> Stream<T> toStream(final T... array) {
        if (isEmpty(array)) {
            return Stream.empty();
        }

        return Stream.of(array);
    }

    /**
     * 转换为数组
     *
     * @param arg 参数
     *
     * @return {@code Object[]}
     */
    public static Object[] toArray(final Object arg) {
        if (null == arg) {
            return new Object[0];
        }

        if (isArray(arg)) {
            return (Object[]) arg;
        }

        if (CollectionUtil.isCollection(arg)) {
            return ((Collection<?>) arg).toArray();
        }

        if (MapUtil.isMap(arg)) {
            return MapUtil.toValueArray((Map<?, ?>) arg);
        }

        return new Object[] { arg };
    }

    /**
     * 转换为数组
     *
     * @param arraySupplier 数组供应商
     * @param valueTest     值测试
     * @param values        值
     *
     * @return {@code T[]}
     */
    @SafeVarargs
    public static <T> T[] toArray(Supplier<T[]> arraySupplier, Predicate<T> valueTest, final T... values) {
        if (isEmpty(values)) {
            return null;
        }

        final List<T> list = new ArrayList<>(values.length);
        for (T value : values) {
            if (valueTest.test(value)) {
                list.add(value);
            }
        }

        return list.toArray(arraySupplier.get());
    }

    /**
     * 将非null值转为新数组
     *
     * @param arraySupplier 数组供应商
     * @param values        值
     *
     * @return {@code T[]}
     */
    @SafeVarargs
    public static <T> T[] toArray(Supplier<T[]> arraySupplier, final T... values) {
        return toArray(arraySupplier, Objects::nonNull, values);
    }

    /**
     * 转为{@code String}数组类型，剔除无意义的空值
     *
     * @param values 值
     *
     * @return {@code String[]}
     *
     * @see StringUtil#isNotBlank(CharSequence)
     */
    public static String[] toStringArray(final String... values) {
        return toArray(() -> new String[0], StringUtil::isNotBlank, values);
    }

    /**
     * 键值对消费
     *
     * @param keyArray   关键数组
     * @param valueArray 值数组
     * @param kvConsumer kv消费者
     */
    public static <K, V> void entryConsumer(final K[] keyArray, final V[] valueArray, BiConsumer<K, V> kvConsumer) {
        if (isEmpty(keyArray) || isEmpty(valueArray)) {
            return;
        }

        final int len = NumberUtil.max(length(keyArray), length(valueArray));
        for (int i = 0; i < len; i++) {
            kvConsumer.accept(get(keyArray, i), get(valueArray, i));
        }
    }

    /**
     * 数组forEach
     *
     * @param array 数组
     * @param peek  消费
     */
    public static <T> void forEach(final T[] array, Consumer<T> peek) {
        if (isEmpty(array)) {
            return;
        }

        for (T t : array) {
            peek.accept(t);
        }
    }


    /**
     * 找到第一个
     *
     * @param array 数组
     *
     * @return {@link T}
     */
    public static <T> T findFirst(T[] array) {
        final int length = length(array);
        return length > 0 ? array[0] : null;
    }

    /**
     * 将多个{@code byte[]}合并为一个{@code byte[]}
     *
     * @param c c
     *
     * @return {@code byte[]}
     */
    public static <T> byte[] merge(List<byte[]> c) {
        if (null == c) {
            return null;
        }

        final FastByteBuffer byteBuffer = new FastByteBuffer();
        c.forEach(byteBuffer::append);
        return byteBuffer.toArray();
    }

    /**
     * 查找
     *
     * @param array 大堆
     * @param test  测验
     *
     * @return {@link Optional}<{@link T}>
     */
    public static <T> Optional<T> find(T[] array, Predicate<T> test) {
        for (final T element : array) {
            if (test.test(element)) {
                return Optional.of(element);
            }
        }
        return Optional.empty();
    }

    /**
     * 用{@code srcArray}从{@code targetArray}的{@code index}位置开始填充
     *
     * @param targetArray 目标数组
     * @param index       填充目标数组的起始位置
     * @param srcArray    原数组
     * @param srcArrayMap 原数组的转换{@link Function#identity()}
     *
     * @throws ArrayIndexOutOfBoundsException 数组索引越界异常,需要保证原始数组的长度为 {@code index + srcArray.length}
     */
    public static <T, S> void padding(T[] targetArray, int index, S[] srcArray, Function<S, T> srcArrayMap) throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < srcArray.length; i++) {
            final S s = srcArray[i];
            @SuppressWarnings("unchecked") final T element = null == srcArrayMap ? (T) s : srcArrayMap.apply(s);
            targetArray[index + i] = element;
        }
    }

    // ----------------------------------------------------------------- instance of

    /**
     * 判定{@code array}是否为{@code elementType}的表示数组类型及其子类型
     *
     * @param array       数组
     * @param elementType 元素类型
     *
     * @return boolean
     */
    public static boolean instanceOf(Object array, Class<?> elementType) {
        final Class<?> componentType = getComponentType(array);
        if (null == componentType) {
            return false;
        }
        return ClassUtil.instanceOf(elementType, componentType);
    }
}
