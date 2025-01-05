package cn.swifthealth.common.tools;

import cn.hutool.core.lang.Tuple;

/**
 * <p>表示3个类型对象的列表</p>
 * <p>创建于 2024/3/21 11:26 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 2.28.0
 */
public class Tuple3<T1, T2, T3> extends Tuple {
    private static final long serialVersionUID = 4112666489909159426L;

    public Tuple3(T1 v1, T2 v2, T3 v3) {
        super(v1, v2, v3);
    }

    public Tuple3(Tuple3<T1, T2, T3> tuple) {
        this(tuple.getV1(), tuple.getV2(), tuple.getV3());
    }

    public T1 getV1() {
        return this.get(0);
    }

    public T2 getV2() {
        return this.get(1);
    }

    public T3 getV3() {
        return this.get(2);
    }

    @Override
    public Tuple3<T1, T2, T3> clone() {
        return new Tuple3<>(this);
    }
}
