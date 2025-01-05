package cn.swifthealth.common.tools;

import cn.hutool.core.bean.NullWrapperBean;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ModifierUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * <p> 反射工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
public class ReflectUtil extends cn.hutool.core.util.ReflectUtil {

    /**
     * 获取注解
     *
     * @param method          方法
     * @param annotationClass 注释类
     *
     * @return {@link A}
     */
    public static <A extends Annotation> A getAnnotation(final Method method, Class<A> annotationClass) {
        return null == method ? null : method.getAnnotation(annotationClass);
    }

    /**
     * 获取注解，从{@link Method}和{@link Class}上依次获取
     *
     * @param clazz           clazz
     * @param method          方法
     * @param annotationClass 注释类
     *
     * @return {@link A}
     */
    public static <A extends Annotation> A getAnnotation(final Class<?> clazz, final Method method, Class<A> annotationClass) {
        final A annotation = null == method ? null : method.getAnnotation(annotationClass);
        if (null != annotation) {
            return annotation;
        }

        return null == clazz ? null : clazz.getAnnotation(annotationClass);
    }

    /**
     * 遍历类中的每个属性
     *
     * @param clazz         clazz
     * @param fieldConsumer 属性消费函数
     */
    public static void eachFields(final Class<?> clazz, Consumer<Field> fieldConsumer) {
        final Field[] fields = getFields(clazz);
        if (null == fields) {
            return;
        }
        for (Field field : fields) {
            fieldConsumer.accept(field);
        }
    }

    /**
     * 遍历每个属性，包含其值
     *
     * @param target        目标
     * @param fieldConsumer 属性消费函数
     */
    public static <T> void eachFields(final T target, BiConsumer<Field, Object> fieldConsumer) {
        eachFields(target.getClass(), f -> fieldConsumer.accept(f, getFieldValue(target, f)));
    }

    /**
     * 获取属性和属性值的映射{@link Map}
     *
     * @param target        目标
     * @param fieldConsumer 属性消费函数
     *
     * @return {@code Map<Field, ?>}
     */
    public static <T> Map<Field, ?> getFieldsValues(final T target, Consumer<Field> fieldConsumer) {
        final Map<Field, Object> fieldsValues = MapUtil.newHashMap(true);
        eachFields(target, fieldsValues::put);
        return fieldsValues;
    }

    /**
     * get方法
     * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
     *
     * <p>
     * 此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回{@code null}。
     * </p>
     *
     * @param clazz      类
     * @param methodName 方法名，如果为空字符串返回{@code null}
     * @param args       参数
     *
     * @return 方法
     *
     * @throws SecurityException 无访问权限抛出异常
     */
    public static Method getMethod(Class<?> clazz, String methodName, Object... args) throws SecurityException {
        return getMethod(clazz, methodName, ClassUtil.getClasses(args));
    }

    /**
     * 调用静态方法
     *
     * @param clazz      clazz
     * @param methodName 方法名
     * @param args       参数列表
     *
     * @return 执行结果
     *
     * @throws UtilException IllegalAccessException等异常包装
     * @see NullWrapperBean
     * @since 3.1.2
     */
    public static <T> T invokeStatic(Class<?> clazz, String methodName, Object... args) throws UtilException {
        Assert.notBlank(methodName, "Method name must be not blank!");

        final Method method = getMethod(clazz, methodName, args);
        if (null == method) {
            throw new UtilException("No such method: [{}] from [{}]", methodName, clazz);
        }
        return invokeStatic(method, args);
    }

    /**
     * 判断某个class是否重写了{@link Object#toString()}方法
     *
     * @param clazz 类
     *
     * @return boolean
     */
    public static boolean isOverrideToString(Class<?> clazz) {
        if (clazz.isArray()) {
            return true;
        }

        final Method superMethod = ReflectUtil.getPublicMethod(Object.class, "toString");
        final Method overrideMethod = ReflectUtil.getPublicMethod(clazz, "toString");

        if (superMethod.getDeclaringClass() != overrideMethod.getDeclaringClass()) {
            return true;
        }

        return false;
    }

    /**
     * 判断某个对象是否重写了{@link Object#toString()}方法
     *
     * @param obj 对象
     *
     * @return boolean
     */
    public static boolean isOverrideToString(Object obj) {
        return null != obj && isOverrideToString(obj.getClass());
    }

    /**
     * 判断一个反射的属性，是不是java标准的封装属性，即含有{@code get}和{@code set}的函数，严格模式下还要求必须是{@code private}修饰符
     *
     * @param field  字段
     * @param strict 严格模式
     *
     * @return boolean
     */
    public static boolean isProperty(Field field, boolean strict) {
        if (null == field) {
            return false;
        }

        // static
        if (ModifierUtil.isStatic(field)) {
            return false;
        }

        // final
        if (ModifierUtil.hasModifier(field, ModifierUtil.ModifierType.FINAL)) {
            return false;
        }

        // public
        if (ModifierUtil.isPublic(field)) {
            return false;
        }

        // 严格模式，校验private 修饰符
        if (strict && !ModifierUtil.hasModifier(field, ModifierUtil.ModifierType.PRIVATE)) {
            return false;
        }

        return true;
    }
}
