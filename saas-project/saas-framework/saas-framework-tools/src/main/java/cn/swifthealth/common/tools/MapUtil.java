package cn.swifthealth.common.tools;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p> Map工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
public class MapUtil extends cn.hutool.core.map.MapUtil {
    /**
     * 是{@link Map}类型
     *
     * @param arg 参数
     *
     * @return boolean
     */
    public static boolean isMap(final Object arg) {
        return arg instanceof Map;
    }

    /**
     * 获得值数组
     *
     * @param map 地图
     *
     * @return {@link Object}
     */
    public static Object[] toValueArray(final Map<?, ?> map) {
        if (isEmpty(map)) {
            return new Object[0];
        }
        return map.values().toArray();
    }

    /**
     * 合并,以右边的为主，将左边的值合并过去，仅使用左边的值覆盖和新增，不删除，处理过的Map会除去无意义的k和v
     *
     * @param left  左
     * @param right 正确
     *
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> merge(Map<K, V> left, Map<K, V> right) {
        if (isEmpty(left)) {
            return null;
        }

        if (isEmpty(right)) {
            return left;
        }

        final Map<K, V> map = newHashMap(left.size() + right.size());
        putEfficient(map, right);

        // 以右边为准，左边覆盖右边的key
        putEfficient(map, left);

        // 删除key,value都无效的值
        removePointless(map);
        return map;
    }


    /**
     * 将有意义的值放入Map中
     *
     * @param map   地图
     * @param key   关键
     * @param value 价值
     *
     * @see ObjectUtil#isBlank(Object)
     */
    public static <K, V> void putEfficientEntry(Map<K, V> map, K key, V value) {
        if (ObjectUtil.isBlank(key) || ObjectUtil.isBlank(value)) {
            return;
        }

        map.put(key, value);
    }

    /**
     * 将有意义的值放入Map中,同时保证不引发{@link NullPointerException}
     *
     * @param map   地图
     * @param key   关键
     * @param value 价值
     */
    public static <K, V> void putEfficientEntryIntoNullableMap(Map<K, V> map, K key, V value) {
        if (null == map || ObjectUtil.isBlank(key) || ObjectUtil.isBlank(value)) {
            return;
        }

        map.put(key, value);
    }

    /**
     * put有意义的值
     *
     * @param map   地图
     * @param other 其他
     *
     * @see #putEfficientEntry(Map, Object, Object)
     */
    public static <K, V> void putEfficient(Map<K, V> map, Map<K, V> other) {
        if (isEmpty(other)) {
            return;
        }

        for (Entry<K, V> entry : other.entrySet()) {
            final K key = entry.getKey();
            final V value = entry.getValue();
            putEfficientEntry(map, key, value);
        }
    }

    /**
     * 删除
     *
     * @param map       地图
     * @param predicate 谓词
     */
    public static <K, V> void remove(Map<K, V> map, Predicate<Entry<K, V>> predicate) {
        if (isEmpty(map)) {
            return;
        }

        final Iterator<Entry<K, V>> iter = map.entrySet().iterator();
        Entry<K, V> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            if (predicate.test(entry)) {
                iter.remove();
            }
        }
    }

    /**
     * 批量删除key
     *
     * @param map        地图
     * @param removeKeys 删除键
     */
    public static <K, V> void remove(Map<K, V> map, Iterable<? extends K> removeKeys) {
        if (isEmpty(map) || CollectionUtil.isEmpty(removeKeys)) {
            return;
        }
        removeKeys.forEach(map::remove);
    }

    /**
     * 删除无意义
     *
     * @param map 地图
     *
     * @see ObjectUtil#isBlank(Object)
     * @see #remove(Map, Predicate)
     */
    public static <K, V> void removePointless(Map<K, V> map) {
        remove(map, e -> ObjectUtil.isBlank(e.getKey()) || ObjectUtil.isBlank(e.getValue()));
    }

    /**
     * 创建不变{@link Entry}对象
     *
     * @param key   关键
     * @param value 价值
     *
     * @return {@link Entry}<{@link K},{@link V}>
     */
    public static <K, V> Entry<K, V> newImmutableEntry(K key, V value) {
        return new EntryImpl<>(key, value);
    }

    /**
     * 创建可变{@link Entry}对象
     *
     * @param key   关键
     * @param value 价值
     *
     * @return {@link Entry}<{@link K}, {@link V}>
     */
    public static <K, V> Entry<K, V> newEntry(K key, V value) {
        return new EntryImpl<>(key, value, false);
    }

    /**
     * 重新转换为新的Map，可转换Key，Value
     *
     * @param map     地图
     * @param mapFunc 映射函数
     *
     * @return {@link Map}<{@link K1}, {@link V1}> 新的Map对象
     */
    public static <K, V, K1, V1> Map<K1, V1> mapTo(Map<K, V> map, Function<Entry<K, V>, Entry<K1, V1>> mapFunc) {
        if (null == map || null == mapFunc) {
            return newHashMap();
        }

        final Map<K1, V1> newMap = newHashMap(map.size());
        for (Entry<K, V> oldEntry : map.entrySet()) {
            Entry<K1, V1> newEntry = mapFunc.apply(oldEntry);
            newMap.put(newEntry.getKey(), newEntry.getValue());
        }
        return newMap;
    }

    /**
     * 任意匹配，返回匹配到的第一个{@link Entry}元素，如果没有匹配到，返回{@code null}
     *
     * @param map 地图
     * @param p   p
     *
     * @return {@link Entry}<{@link K}, {@link V}>
     */
    public static <K, V> Entry<K, V> anyMatch(Map<K, V> map, BiPredicate<K, V> p) {
        for (Entry<K, V> entry : map.entrySet()) {
            final K key = entry.getKey();
            final V value = entry.getValue();
            if (p.test(key, value)) {
                return newImmutableEntry(key, value);
            }
        }
        return null;
    }

    /**
     * 比较值键
     *
     * @param valueCmp cmp价值
     * @param keyCmp   关键cmp
     *
     * @return {@link Comparator}<{@link Entry}<{@link K}, {@link V}>>
     */
    public static <K, V> Comparator<Entry<K, V>> comparingValueKey(Comparator<? super V> valueCmp, Comparator<? super K> keyCmp) {
        return ((Comparator<Entry<K, V>> & Serializable) (c1, c2) -> valueCmp.compare(c1.getValue(), c2.getValue()))
            .thenComparing((Comparator<Entry<K, V>> & Serializable) (c1, c2) -> keyCmp.compare(c1.getKey(), c2.getKey()));
    }

    /**
     * 比较键值
     *
     * @param keyCmp   关键cmp
     * @param valueCmp cmp价值
     *
     * @return {@link Comparator}<{@link Entry}<{@link K}, {@link V}>>
     */
    public static <K, V> Comparator<Entry<K, V>> comparingKeyValue(Comparator<? super K> keyCmp, Comparator<? super V> valueCmp) {
        return ((Comparator<Entry<K, V>> & Serializable) (c1, c2) -> keyCmp.compare(c1.getKey(), c2.getKey()))
            .thenComparing((Comparator<Entry<K, V>> & Serializable) (c1, c2) -> valueCmp.compare(c1.getValue(), c2.getValue()));
    }


    /**
     * 递归搜索非空的值
     *
     * @param map 地图
     * @param key 关键
     *
     * @return {@link Object}
     */
    @SuppressWarnings("unchecked")
    public static <K> Object recursiveSearch(Map<K, ?> map, K key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }

        for (Entry<K, ?> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (!(value instanceof Map)) {
                continue;
            }
            Object nestedValue = recursiveSearch((Map<K, ?>) value, key);
            if (nestedValue != null) {
                return nestedValue;
            }

        }
        return null;
    }

    /**
     * 类似于缓存的操作，如果存在则查询缓存，否则获取到值后，放入Map,再返回
     *
     * @param map           地图
     * @param key           关键
     * @param valueSupplier 价值供应商
     *
     * @return {@link V}
     */
    public static <K, V> V putIfPresent(Map<K, V> map, K key, Supplier<V> valueSupplier) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        final V newValue = valueSupplier.get();
        map.put(key, newValue);
        return newValue;
    }

    /**
     * 查找{@code keys}中的值不存在{@code Map}的第一条记录
     *
     * @param keys 按键
     * @param map  地图
     *
     * @return {@link Optional}<{@link K}>
     */
    public static <K, V> Optional<K> findNoExistKey(Collection<K> keys, Map<K, V> map) {
        for (final K key : keys) {
            if (!map.containsKey(key)) {
                return Optional.ofNullable(key);
            }
        }
        return Optional.empty();
    }

    /**
     * 新建一个Map,该对象如果不为空，copy到新的Map中
     *
     * @param map 地图
     *
     * @return Map对象
     *
     * @since 3.0.0
     */
    public static <K, V> Map<K, V> newHashMap(Map<K, V> map) {
        if (null == map) {
            return newHashMap();
        }

        if (map instanceof LinkedHashMap) {
            return new LinkedHashMap<>(map);
        }

        return new HashMap<>(map);
    }

    /**
     * 放
     *
     * @param map   地图
     * @param entry 进入
     */
    public static <K, V> void put(Map<K, V> map, Entry<K, V> entry) {
        if (null == entry) {
            return;
        }
        map.put(entry.getKey(), entry.getValue());
    }

    /**
     * 全部投入
     *
     * @param map        地图
     * @param entryArray 进入
     */
    public static <K, V> void putAll(Map<K, V> map, Entry<K, V>[] entryArray) {
        if (ArrayUtil.isEmpty(entryArray)) {
            return;
        }

        for (final Entry<K, V> entry : entryArray) {
            put(map, entry);
        }
    }

    /**
     * 取{@link Map}中的第一个键值对，取决于{@link Map}的实现是否有序
     *
     * @param map 地图
     *
     * @return {@link Entry}<{@link K}, {@link V}>
     */
    public static <K, V> Entry<K, V> findFirst(Map<K, V> map) {
        if (isEmpty(map)) {
            return null;
        }

        for (final Entry<K, V> entry : map.entrySet()) {
            return entry;
        }
        return null;
    }

    /**
     * 具有未知Key
     *
     * @param parentKeys 父键
     * @param childMap   子图
     *
     * @return boolean
     */
    public static <K> boolean hasUnknownKey(Collection<K> parentKeys, Map<K, ?> childMap) {
        if (isEmpty(childMap)) {
            return CollectionUtil.isNotEmpty(parentKeys);
        }

        for (final Entry<K, ?> childEntry : childMap.entrySet()) {
            K key = childEntry.getKey();
            if (!parentKeys.contains(key)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 获取值,忽略值泛型
     *
     * @param map 地图
     * @param key 钥匙
     *
     * @return {@link V}
     */
    @SuppressWarnings("unchecked")
    public static <K, V> V getIgnoreValueType(Map<K, ?> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return (V) map.get(key);
    }
}
