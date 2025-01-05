package cn.swifthealth.common.tools;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Filter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <p> 集合工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class CollectionUtil extends CollUtil {


    /**
     * 转为{@link Stream}类型，比较安全的方式，不会产生{@link NullPointerException}
     *
     * @param c c
     *
     * @return {@link Stream}<{@link T}>
     */
    public static <T> Stream<T> toStream(final Collection<T> c) {
        return isEmpty(c) ? Stream.empty() : c.stream();
    }


    /**
     * 非空循环
     *
     * @param iterator 迭代器
     * @param doFunc   do something
     */
    public static <T> void forEach(final Iterable<T> iterator, final java.util.function.Consumer<T> doFunc) {
        if (null == iterator) {
            return;
        }
        for (T t : iterator) {
            doFunc.accept(t);
        }
    }

    /**
     * 是集合类型
     *
     * @param arg 参数
     *
     * @return boolean
     */
    public static boolean isCollection(final Object arg) {
        return arg instanceof Collection;
    }

    /**
     * 空列表
     *
     * @return {@link List}<{@link E}>
     */
    public static <E> List<E> emptyList() {
        return new ArrayList<>(0);
    }

    /**
     * 转为{@link List}类型
     *
     * @param c c
     *
     * @return {@link List}<{@link E}>
     */
    public static <E> List<E> toList(Collection<E> c) {
        return c instanceof List ? (List<E>) c : ListUtil.toList(c);
    }

    /**
     * 查找第{@code index}被{@code test}命中的元素
     *
     * @param elements 元素
     * @param test     测试
     * @param index    指数
     *
     * @return {@link T}
     */
    public static <T> T find(Iterable<T> elements, Predicate<T> test, int index) {
        if (null == elements) {
            return null;
        }

        int count = 0;
        for (final T next : elements) {
            if (!test.test(next)) {
                continue;
            }

            if (++count == index) {
                return next;
            }
        }

        return null;
    }

    /**
     * 查找第{@code index}不为null的元素
     *
     * @param elements 元素
     * @param index    指数
     *
     * @return {@link T}
     */
    public static <T> T findNoNull(Iterable<T> elements, int index) {
        return find(elements, Objects::nonNull, index);
    }

    /**
     * 查找第1个不为null的元素
     *
     * @param elements 元素
     *
     * @return {@link T}
     */
    public static <T> T findFirstNoNull(Iterable<T> elements) {
        return find(elements, Objects::nonNull, 1);
    }

    /**
     * 将元素推入List的头
     *
     * @param c        c
     * @param elements 元素
     *
     * @return {@link List}<{@link T}>
     */
    @SafeVarargs
    public static <T> List<T> insertHead(List<T> c, T... elements) {
        if (null != c && null != elements) {
            c.addAll(0, toList(elements));
        }
        return c;
    }

    /**
     * 集合在另一个数组中是否至少包含一个元素，即是两个集合是否至少有一个共同的元素
     *
     * @param coll  coll
     * @param array 大堆
     *
     * @return 其中一个集合在另一个集合中是否至少包含一个元素
     *
     * @see #intersection
     * @since 2.1
     */
    public static boolean containsAny(Collection<?> coll, Object[] array) {
        if (isEmpty(coll) || ArrayUtil.isEmpty(array)) {
            return false;
        }
        if (coll.size() < array.length) {
            for (Object object : coll) {
                if (ArrayUtil.contains(array, object)) {
                    return true;
                }
            }
            return false;
        }
        for (Object object : array) {
            if (coll.contains(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 挑选第一个相同的元素
     *
     * @param coll  coll
     * @param array 大堆
     *
     * @return boolean
     */
    public static <T> Optional<T> takeWhileFirstIdenticalElement(Collection<T> coll, T[] array) {
        if (isEmpty(coll) || ArrayUtil.isEmpty(array)) {
            return Optional.empty();
        }

        if (coll.size() < array.length) {
            for (T object : coll) {
                if (ArrayUtil.contains(array, object)) {
                    return Optional.of(object);
                }
            }
            return Optional.empty();
        }

        for (T object : array) {
            if (coll.contains(object)) {
                return Optional.of(object);
            }
        }
        return Optional.empty();
    }

    /**
     * 有很多
     *
     * @param c c
     *
     * @return boolean
     */
    public static boolean hasMany(Collection<?> c) {
        return null != c && c.size() > 1;
    }

    /**
     * 只有一个
     *
     * @param c c
     *
     * @return boolean
     */
    public static boolean hasOnlyOne(Collection<?> c) {
        return null != c && c.size() == 1;
    }

    /**
     * 添加元素
     *
     * @param c       c
     * @param element 元素
     *
     * @return boolean
     */
    public static <E> boolean add(Collection<E> c, E element) {
        if (null == element) {
            return false;
        }
        return c.add(element);
    }

    /**
     * 删除元素
     *
     * @param c       c
     * @param element 元素
     *
     * @return boolean
     */
    public static <E> boolean remove(Collection<E> c, E element) {
        if (null == element) {
            return false;
        }
        return c.remove(element);
    }

    /**
     * 将某个过滤到的不为null的元素移动到集合的末尾，通常适用于{@link ArrayList}和{@link java.util.LinkedList}
     *
     * @param c      c
     * @param filter 滤器
     */
    public static <E> void moveLast(Collection<E> c, Filter<E> filter) {
        final E element = findOne(c, filter);
        remove(c, element);
        add(c, element);
    }

    /**
     * 如果提供的集合为空集合，则返回{@code null}
     *
     * @param list 列表
     *
     * @return {@code List<T>}
     */
    public static <T> List<T> nullIfEmpty(List<T> list) {
        return isEmpty(list) ? null : list;
    }

    /**
     * 如果提供的集合为空集合，则返回{@code null}
     *
     * @param set 集合
     *
     * @return {@code Set<T>}
     */
    public static <T> Set<T> nullIfEmpty(Set<T> set) {
        return isEmpty(set) ? null : set;
    }
}
