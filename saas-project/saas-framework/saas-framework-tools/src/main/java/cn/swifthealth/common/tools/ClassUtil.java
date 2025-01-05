package cn.swifthealth.common.tools;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.AntPathMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * <p> Class相关工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
public class ClassUtil extends cn.hutool.core.util.ClassUtil {


    /**
     * 从权限定名中得到类名
     *
     * @param referenceName 引用名称
     *
     * @return {@link String}
     */
    public static String getClassName(final String referenceName) {
        if (StringUtil.isBlank(referenceName)) {
            return null;
        }

        final String objectName = StringUtil.subAfter(referenceName, StringUtil.DOT, true);
        // 最后一个串的首字母大写，说明是类的权限定名
        if (StringUtil.isUpperFirst(objectName)) {
            return referenceName;
        }

        // 说明最后一节是方法名，截取前半节即可
        return StringUtil.subBefore(referenceName, StringUtil.DOT, true);
    }

    /**
     * 从权限定名中得到函数名
     *
     * @param referenceName 引用名称
     *
     * @return {@link String}
     */
    public static String getMethodName(final String referenceName) {
        if (StringUtil.isBlank(referenceName)) {
            return null;
        }

        final String objectName = StringUtil.subAfter(referenceName, StringUtil.DOT, true);
        // 最后一个串的首字母大写，说明是类的权限定名，未包含方法名
        if (StringUtil.isUpperFirst(objectName)) {
            return null;
        }

        // 说明最后一节是方法名
        return objectName;
    }

    /**
     * 从权限定名中获取函数名
     *
     * @param referenceName 引用名称
     *
     * @return {@link Method}
     */
    public static Method getMethod(final String referenceName) {
        final String className = getClassName(referenceName);
        if (null == className) {
            return null;
        }
        final Class<?> clazz = loadClass(className);
        return ReflectUtil.getMethodByName(clazz, getMethodName(referenceName));
    }

    /**
     * 从权限定名中获取到方法的注解
     *
     * @param annotationClass 注释类
     * @param referenceName   引用名称
     *
     * @return {@link A}
     */
    public static <A extends Annotation> A getAnnotation(final String referenceName, Class<A> annotationClass) {
        return ReflectUtil.getAnnotation(getMethod(referenceName), annotationClass);
    }

    /**
     * 从权限定名获取注解，从{@link Method}和{@link Class}上依次获取
     *
     * @param annotationClass 注释类
     * @param referenceName   引用名称
     *
     * @return {@link A}
     */
    public static <A extends Annotation> A resolveAnnotation(final String referenceName, Class<A> annotationClass) {
        final String className = getClassName(referenceName);
        if (StringUtil.isBlank(className)) {
            return null;
        }
        final Class<Object> objectClass = loadClass(className);
        final Method method = getMethod(referenceName);
        return ReflectUtil.getAnnotation(objectClass, method, annotationClass);
    }

    /**
     * 存在指定名称的类
     *
     * @param className 类名
     *
     * @return boolean
     */
    public static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }


    /**
     * 属于
     *
     * @param predicateClass 谓词类
     * @param packageName    程序包名称
     *
     * @return boolean
     */
    public static boolean belongTo(Class<?> predicateClass, String packageName) {
        return getPackage(predicateClass).startsWith(packageName);
    }

    /**
     * 匹配程序包
     *
     * @param predicateClass     谓词类
     * @param packageNamePattern 程序包名称模式
     *
     * @return boolean
     */
    public static boolean matchPackage(Class<?> predicateClass, String packageNamePattern) {
        final AntPathMatcher antPathMatcher = Singleton.get(AntPathMatcher.class.getName() + "_" + StringUtil.DOT,
                                                            () -> new AntPathMatcher(StringUtil.DOT));
        return antPathMatcher.match(packageNamePattern, getPackage(predicateClass));
    }

    /**
     * 获取该类继承链上的所有父类
     *
     * @param clazz clazz
     *
     * @return {@link List}
     */
    public static List<Class<?>> getSuperClasses(Class<?> clazz) {
        final List<Class<?>> list = new ArrayList<>();
        Class<?> superClass = clazz;
        while (true) {
            list.add(superClass);
            if (superClass == Object.class) {
                return list;
            }
            superClass = superClass.getSuperclass();
        }
    }

    /**
     * 获取该类所有的接口
     *
     * @param clazz clazz
     *
     * @return {@link List}
     */
    public static List<Class<?>> getSuperInterfaces(Class<?> clazz) {
        final List<Class<?>> list = new ArrayList<>();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (final Class<?> interfaceClass : interfaces) {
            list.add(interfaceClass);
            list.addAll(getSuperInterfaces(interfaceClass));
        }

        return list;
    }

    /**
     * 获取某个类或超类或接口,超接口的列表
     *
     * @param clazz clazz
     *
     * @return {@link List}
     */
    public static List<Class<?>> getSuperClassOrInterfaces(Class<?> clazz) {
        final List<Class<?>> list = new ArrayList<>();
        Class<?> superClass = clazz;
        while (superClass != null) {
            if (superClass == Object.class) {
                list.add(superClass);
                return list;
            }

            list.add(superClass);
            list.addAll(getSuperInterfaces(superClass));
            superClass = superClass.getSuperclass();
        }

        return list;
    }

    /**
     * 类似于{@code instance of}关键字,判定{@code obj}是否是{@code classOrInterfaceNames}列表中,任意一个的实例
     *
     * @param obj                   对象实例
     * @param classOrInterfaceNames 类或接口全限定名称
     *
     * @return boolean
     */
    public static boolean instanceOf(Object obj, String... classOrInterfaceNames) {
        if (null == obj || null == classOrInterfaceNames) {
            return false;
        }

        final List<Class<?>> superClassOrInterfaces = getSuperClassOrInterfaces(obj.getClass());
        String instanceClassName;
        for (final Class<?> superClassOrInterface : superClassOrInterfaces) {
            for (final String className : classOrInterfaceNames) {
                instanceClassName = superClassOrInterface.getName();
                if (instanceClassName.equals(className)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将{@link Class#isAssignableFrom(Class)}简化为{@code instance of}关键字语法
     *
     * @param instanceClass 实例类
     * @param target        目标
     *
     * @return boolean
     *
     * @see Class#isAssignableFrom(Class)
     */
    public static boolean instanceOf(Class<?> instanceClass, Class<?> target) {
        if (instanceClass.isAssignableFrom(target)) {
            return true;
        }
        return false;
    }

    /**
     * 获取方法id
     *
     * @param method     方法
     * @param simpleName 简单名称
     *
     * @return {@link String}
     */
    public static String getMethodId(Method method, boolean simpleName) {
        final Class<?> declaringClass = method.getDeclaringClass();
        // 参数类型
        final String parameter = Arrays.stream(method.getParameterTypes())
                                       .map(c -> simpleName ? c.getSimpleName() : c.getTypeName())
                                       .collect(Collectors.joining(",", "(", ")"));

        final StringJoiner methodId
            = new StringJoiner(" ", (simpleName ? declaringClass.getSimpleName() : declaringClass.getName()) + ".", "");
        // 访问修饰符
        methodId.add(Modifier.toString(method.getModifiers()))
                // 返回类型
                .add(simpleName ? method.getReturnType().getSimpleName() : method.getReturnType().getName())
                // 方法名和参数列表
                .add(method.getName() + parameter);

        return methodId.toString();
    }


    /**
     * 判断是不是Jdk内部的类
     *
     * @param clazz 克拉兹
     *
     * @return boolean
     *
     * @see cn.hutool.core.util.ClassUtil#isJdkClass(Class)
     */
    public static boolean isJdkClass(Class<?> clazz) {
        final Package objectPackage = clazz.getPackage();
        if (null == objectPackage) {
            return false;
        }
        final String objectPackageName = objectPackage.getName();
        if (objectPackageName.startsWith("java.")) {
            return true;
        }

        if (objectPackageName.startsWith("javax.")) {
            return true;
        }

        if (objectPackageName.startsWith("jakarta.")) {
            return true;
        }

        return clazz.getClassLoader() == null;
    }

    /**
     * 判定两个类是否来自同一个jar
     *
     * @param a a
     * @param b b
     *
     * @return boolean
     */
    public static boolean isSameJar(Class<?> a, Class<?> b) {
        final String ajPath = getLocationPath(a);
        final String bjPath = getLocationPath(b);
        if (ajPath == null && bjPath == null) {
            return true;
        }

        return StringUtil.equals(ajPath, bjPath);
    }
}
