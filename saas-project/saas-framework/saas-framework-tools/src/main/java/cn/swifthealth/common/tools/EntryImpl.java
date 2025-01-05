package cn.swifthealth.common.tools;

import java.util.Map;
import java.util.Objects;

/**
 * <p> {@link Map.Entry}实现 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 2.25.0
 */
public class EntryImpl<K, V> implements Map.Entry<K, V> {

    private K key;

    private V value;

    private boolean readOnly = true;

    public EntryImpl(K key, V value, boolean readOnly) {
        this.key = key;
        this.value = value;
        this.readOnly = readOnly;
    }

    public EntryImpl(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        if (this.readOnly) {
            throw new UnsupportedOperationException();
        }
        final V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    public K setKey(K key) {
        if (this.readOnly) {
            throw new UnsupportedOperationException();
        }
        final K oldKey = this.key;
        this.key = key;
        return oldKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, readOnly);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntryImpl<?, ?> entry = (EntryImpl<?, ?>) o;
        return readOnly == entry.readOnly && Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
    }
}
