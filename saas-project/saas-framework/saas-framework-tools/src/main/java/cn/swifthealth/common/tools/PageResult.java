package cn.swifthealth.common.tools;

import cn.swifthealth.common.validation.Assert;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p> 分页结果对象 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 2.22.0
 */
@Setter
@Getter
@ToString
@SuppressWarnings("unused")
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -9106492000763170079L;

    /**
     * 总元素
     */
    private long totalElements = 0;

    /**
     * 总页
     */
    private long totalPages = 0;

    /**
     * 是否最后一页
     */
    private Boolean last = false;

    /**
     * 是否第一页
     */
    private Boolean first = true;

    /**
     * 大小
     */
    private long size = 0;

    /**
     * 数量
     */
    private long number = 0;

    /**
     * 元素数量
     */
    private long numberOfElements = 0;

    /**
     * 模型
     */
    @Getter
    private List<T> models = new ArrayList<>();

    /**
     * 列表求和兼容字段，后续考虑具体的VO代替
     */
    @Deprecated
    private Object total;

    @ToString.Exclude
    private Stream<T> modelStream;

    public PageResult() {
    }

    /**
     * 空
     *
     * @return {@link PageResult}<{@link T}>
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>();
    }

    /**
     * 自定义分页行为，通过Lambda表达式{@code pagination}自定义{@code totalModels}的处理方式
     *
     * @param pageNo      起始页
     * @param size        页面大小
     * @param totalModels 总记录列表
     * @param pagination  分页函数
     *
     * @return {@link PageResult}<{@link T}>
     */
    public static <T> PageResult<T> of(Integer pageNo, Integer size, Collection<T> totalModels, Function<Collection<T>, Collection<T>> pagination) {
        final int totalElements = CollectionUtil.size(totalModels);
        final PageResult<T> pageResult = new PageResult<>();
        pageResult.totalElements = totalElements;
        pageResult.size = size;
        pageResult.number = pageNo;
        pageResult.models = CollectionUtil.toList(pagination.apply(totalModels));
        pageResult.first = pageNo == 0;

        // limit 合理化
        final int rationalizationLimit = 0 == size ? 1 : size;
        pageResult.totalPages = totalElements / rationalizationLimit;
        if (totalElements % rationalizationLimit > 0) {
            pageResult.totalPages += 1;
        }
        if (pageResult.totalPages == 0) {
            pageResult.last = true;
        }
        pageResult.numberOfElements = rationalizationLimit;
        if (pageNo == (pageResult.totalPages - 1)) {
            pageResult.last = true;
            if (pageNo == 0) {
                pageResult.numberOfElements = totalElements;
            } else {
                pageResult.numberOfElements = totalElements - ((long) rationalizationLimit * pageNo);
            }
        }

        return pageResult;
    }

    /**
     * 构建假分页数据（实际是全部数据）
     *
     * @param pageNo pageNo
     * @param size   页面大小
     * @param models 模型
     *
     * @return {@link PageResult}<{@link T}>
     */
    public static <T> PageResult<T> of(Integer pageNo, Integer size, Collection<T> models) {
        final int totalElements = CollectionUtil.size(models);
        final PageResult<T> pageResult = new PageResult<>();
        pageResult.totalElements = totalElements;
        pageResult.size = size;
        pageResult.number = pageNo;
        pageResult.models = CollectionUtil.toList(models);
        return of(pageNo, size, models, Function.identity());
    }

    /**
     * 慎用！！！
     * 基于内存的分页，实际的分页偏移量，数据随着分页的参数变化
     *
     * @param pageNo 页面没有
     * @param size   页面大小
     * @param models 模型
     *
     * @return {@link PageResult}<{@link T}>
     */
    public static <T> PageResult<T> ofMemory(Integer pageNo, Integer size, Collection<T> models) {
        return of(pageNo, size, models, m -> CollectionUtil.page(pageNo, size, CollectionUtil.toList(m)));
    }

    /**
     * 获取当前的Stream对象
     *
     * @return {@link Stream}<{@link T}>
     */
    protected Stream<T> stream() {
        return ObjectUtil.orElse(this.modelStream, () -> CollectionUtil.toStream(this.models));
    }

    /**
     * 遍历
     *
     * @param func 函数
     */
    public void forEach(Consumer<T> func) {
        this.peek(func).collect();
    }

    /**
     * 偷看
     *
     * @param func 函数
     *
     * @return {@link PageResult}<{@link T}>
     */
    public PageResult<T> peek(Consumer<T> func) {
        this.modelStream = this.stream().peek(func);
        return this;
    }

    /**
     * 排序
     *
     * @param comparator 比较器
     *
     * @return {@link PageResult}<{@link T}>
     */
    public PageResult<T> sort(Comparator<? super T> comparator) {
        this.modelStream = this.stream().sorted(comparator);
        return this;
    }

    /**
     * 将{@link #models}转换为另一个类型
     *
     * @param func 函数
     *
     * @return {@link PageResult}<{@link R}>
     */
    @SuppressWarnings("unchecked")
    public <R> PageResult<R> map(Function<T, R> func) {
        final PageResult<R> current = (PageResult<R>) this;
        current.modelStream = this.stream().map(func);
        current.models = null;
        return current;
    }

    /**
     * 收集,将内部的流式处理终止，并将结果覆盖到原始数据中
     *
     * @return {@link PageResult}<{@link T}>
     */
    public PageResult<T> collect() {
        if (null == this.modelStream) {
            throw new IllegalStateException("code error ,modelStream closed !");
        }
        // stream closed ,clear stream reference
        this.models = this.modelStream.collect(Collectors.toList());
        this.modelStream = null;
        return this;
    }

    /**
     * 是否为空
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return CollectionUtil.isEmpty(this.models);
    }

    /**
     * 创建合理化的Page对象
     *
     * @param page    起始页
     * @param size    大小
     * @param creator page对象创建器
     *
     * @return {@link R}
     */
    private static <R> R newReasonablePage(int page, int size, PageCreator<R> creator) {
        final int rp = page < 0 ? 1 : page + 1;
        final int rs = size <= 0 ? 1 : size;
        Assert.isTrue(page <= 10000,
                      () -> new IllegalArgumentException("The 'pageNumber' are too large . Please try adding filter conditions !"));
        Assert.isTrue(size <= 10000,
                      () -> new IllegalArgumentException("The 'pageSize' are too large. Please try adding filter conditions !"));
        return creator.apply(rp, rs);
    }

    /**
     * page对象创建器
     *
     * @author admin
     */
    interface PageCreator<T> {
        T apply(int page, int size);
    }

    /**
     * 数据库层面的分页实现
     *
     * @author witt
     */
    public static class Db {

        /**
         * 汇总并且分页
         *
         * @param pageNum 输入页面num
         * @param size    输入页面大小
         * @param select  选择
         *
         * @return {@link PageResult}<{@link T}>
         */
        public static <T> PageResult<T> page(int pageNum, int size, ISelect select) {
            try (Page<T> page = newReasonablePage(pageNum, size, PageHelper::startPage).doSelectPage(select)) {
                return map2PageResult(page.toPageInfo());
            }
        }

        /**
         * 查询所有数据并汇总记录数，不分页
         *
         * @param select 选择
         *
         * @return {@link PageResult}<{@link T}>
         */
        public static <T> PageResult<T> pageAll(ISelect select) {
            try (Page<T> page = PageHelper.startPage(1, 0).doSelectPage(select)) {
                return map2PageResult(page.toPageInfo());
            }
        }

        /**
         * 仅分页
         *
         * @param pageNum 输入页面num
         * @param size    输入页面大小
         * @param select  选择
         *
         * @return {@link PageResult}<{@link T}>
         */
        public static <T> PageResult<T> paging(int pageNum, int size, ISelect select) {
            try (Page<T> page = newReasonablePage(pageNum, size, PageHelper::startPage).count(false).doSelectPage(select)) {
                return map2PageResult(page.toPageInfo());
            }
        }

        /**
         * {@code count}总记录数，不进行分页查询
         *
         * @param select 查询函数
         *
         * @return long
         */
        public static long count(ISelect select) {
            try (Page<?> page = PageHelper.startPage(1, -1)) {
                return page.doCount(select);
            }
        }

        /**
         * {@link Page}转换为{@link PageResult}
         *
         * @param page 页面
         *
         * @return {@link PageResult}<{@link T}>
         */
        private static <T> PageResult<T> map2PageResult(PageInfo<T> page) {
            final PageResult<T> result = new PageResult<>();
            result.number = page.getPageNum() - 1;
            result.totalElements = page.getTotal();
            result.totalPages = page.getPages();
            result.last = page.isIsLastPage();
            result.first = page.isIsFirstPage();
            result.size = page.getPageSize();
            result.models = page.getList();
            result.numberOfElements = page.getSize();
            return result;
        }
    }
}
