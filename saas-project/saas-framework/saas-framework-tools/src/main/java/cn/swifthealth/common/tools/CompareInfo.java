package cn.swifthealth.common.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <p> 比较信息 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
public class CompareInfo<T> {

    /**
     * 空大
     */
    private boolean nullGreater;

    private List<Function<T, ?>> fields;

    /**
     * 以最低优先级排序，优先排在最前面的元素
     */
    private List<?> lowestValues;

    /**
     * 以最高优先级排序，优先排在最后面的元素
     */
    private List<?> highestValues;

    /**
     * 最高自我比较，如果类本身实现了比较器，优先自我比较
     */
    private boolean highestSelfCompare = true;

    private CompareInfo() {
    }

    @SafeVarargs
    public static <T> CompareInfo<T> of(final boolean isNullGreater, final Function<T, ?>... fields) {
        final CompareInfo<T> compareInfo = new CompareInfo<>();
        return compareInfo.addCompareFields(fields).setNullGreater(isNullGreater);
    }

    @SafeVarargs
    public static <T> CompareInfo<T> of(final Function<T, ?>... fields) {
        final CompareInfo<T> compareInfo = new CompareInfo<>();
        return compareInfo.addCompareFields(fields);
    }

    /**
     * 添加比较字段，在两个对象都非空时，逐个对字段值进行比较
     *
     * @param fields 字段
     *
     * @return {@link CompareInfo}<{@link T}>
     */
    public CompareInfo<T> addCompareFields(final Function<T, ?>[] fields) {
        if (ArrayUtil.isEmpty(fields)) {
            return this;
        }

        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }

        CollectionUtil.addAll(this.fields, fields);
        return this;
    }

    /**
     * 设置空值的排序方式
     *
     * @param isNullGreater 大空
     *
     * @return {@link CompareInfo}<{@link T}>
     */
    public CompareInfo<T> setNullGreater(final boolean isNullGreater) {
        this.nullGreater = isNullGreater;
        return this;
    }

    /**
     * 设置对象自实现的比较器优先
     *
     * @param highestSelfCompare 最高自我比较
     *
     * @return {@link CompareInfo}<{@link T}>
     */
    public CompareInfo<T> setHighestSelfCompare(final boolean highestSelfCompare) {
        this.highestSelfCompare = highestSelfCompare;
        return this;
    }

    /**
     * 添加优先排在最后面的内容值
     *
     * @param values 值
     *
     * @return {@link CompareInfo}<{@link T}>
     */
    public CompareInfo<T> addHighestValues(final Object... values) {
        if (ArrayUtil.isEmpty(values)) {
            return this;
        }

        if (this.highestValues == null) {
            this.highestValues = new ArrayList<>();
        }

        CollectionUtil.addAll(this.highestValues, values);
        return this;
    }

    /**
     * 添加优先排在最前面的内容值
     *
     * @param values 值
     *
     * @return {@link CompareInfo}<{@link T}>
     */
    public CompareInfo<T> addLowestValues(final Object... values) {
        if (ArrayUtil.isEmpty(values)) {
            return this;
        }

        if (this.lowestValues == null) {
            this.lowestValues = new ArrayList<>();
        }

        CollectionUtil.addAll(this.lowestValues, values);
        return this;
    }

    /**
     * 空值放最后
     *
     * @return boolean
     */
    protected boolean isNullGreater() {
        return this.nullGreater;
    }

    protected <X> Integer lowest(final X o1, final X o2) {
        if (null == this.lowestValues) {
            return null;
        }
        final int indexOfO1 = this.lowestValues.indexOf(o1);
        final int indexOfO2 = this.lowestValues.indexOf(o2);

        if (-1 == indexOfO1 && -1 == indexOfO2) {
            return null;
        }

        if (indexOfO1 == -1) {
            return 1;
        }

        if (indexOfO2 == -1) {
            return -1;
        }

        return indexOfO1 <= indexOfO2 ? -1 : 1;
    }

    protected <X> Integer highest(final X o1, final X o2) {
        if (null == this.highestValues) {
            return null;
        }
        final int indexOfO1 = this.highestValues.indexOf(o1);
        final int indexOfO2 = this.highestValues.indexOf(o2);

        if (-1 == indexOfO1 && -1 == indexOfO2) {
            return null;
        }

        if (indexOfO1 == -1) {
            return -1;
        }

        if (indexOfO2 == -1) {
            return 1;
        }

        return indexOfO1 <= indexOfO2 ? 1 : -1;
    }

    /**
     * 本地比较，返回null时，当前未比较出结果
     *
     * @param o1 o1群
     * @param o2 o2
     *
     * @return int
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <X> Integer nativeCompare(final X o1, final X o2) {
        if (o1 == o2) {
            return 0;
        }

        if (null == o1) {
            // null 排在后面
            return this.nullGreater ? 1 : -1;
        }

        if (null == o2) {
            return this.nullGreater ? -1 : 1;
        }

        // 低优先级优先
        final Integer lowest = this.lowest(o1, o2);
        if (null != lowest) {
            return lowest;
        }

        final Integer highest = this.highest(o1, o2);
        if (null != highest) {
            return highest;
        }

        // 自己实现了比较器
        final boolean implCompare = this.highestSelfCompare && o1 instanceof Comparable && o2 instanceof Comparable;
        if (implCompare) {
            //如果bean可比较，直接比较bean
            return ((Comparable) o1).compareTo(o2);
        }

        // 本地没有比较出结果
        return null;
    }

    public <X> int compare(final X o1, final X o2) {
        final Integer nativeCompare = nativeCompare(o1, o2);
        return null == nativeCompare ? CompareUtil.compare(o1, o2, this.nullGreater) : nativeCompare;
    }

    /**
     * 比较属性
     *
     * @param o1 o1群
     * @param o2 o2
     *
     * @return {@link Integer}
     */
    public <X> Integer compareField(final T o1, final T o2) {
        // 为空，不需要比较属性，如果类定义了自己的比较器，仍然忽略
        if (CollectionUtil.isEmpty(this.fields)) {
            return null;
        }

        for (Function<T, ?> field : this.fields) {
            final Object fieldOfO1 = field.apply(o1);
            final Object fieldOfO2 = field.apply(o2);

            final int fieldEquals = this.compare(fieldOfO1, fieldOfO2);

            // 已经分出先后，直接返回
            if (0 != fieldEquals) {
                return fieldEquals;
            }
        }

        // 属性全部比较结束
        return 0;
    }

    /**
     * 比较，对象比较，属性比较，如果引用类型未实现比较器，结果为空
     *
     * @param o1 o1群
     * @param o2 o2
     *
     * @return {@link Integer}
     */
    public Integer compareTo(final T o1, final T o2) {
        // 先比较对象
        final Integer nativeCompare = this.nativeCompare(o1, o2);
        // 本地比较，结果已出来
        if (null != nativeCompare) {
            return nativeCompare;
        }

        // 比较属性
        // 属性比较，结果已出来
        return this.compareField(o1, o2);
    }
}
