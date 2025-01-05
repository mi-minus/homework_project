package cn.swifthealth.common.tools;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * Copyright (C) @2022 minus@swifthealth.cn
 * <p>文件名称: StringUtil </p>
 * <p>描述: 注意！！该类在3.0务必合并 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 */
public class StringUtil extends StrUtil {

    /**
     * A String equivalent of {@code Content-Type}.
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * 英文标点符号?
     */
    public static final String EN_QUESTION_MARK = "?";

    /**
     * 星号
     */
    public static final String ASTERISK = "*";

    /**
<<<<<<< HEAD
     * 双引号{@code "}
     */
    public static final String DOUBLE_QUOTES = CharacterUtil.toString(CharPool.DOUBLE_QUOTES);

    /**
     * 大写{@code null}
     */
    public static final String UPPERCASE_NULL = "NULL";

    /**
     * 分号
     */
    public static final String SEMICOLON = ";";

    /**
=======
>>>>>>> e4eec972f7e70ef125e13c559ab3844e16415d96
     * 转为字符串,如果为空，转为{@code null}而不是{@code ""}
     *
     * @param t t
     *
     * @return {@link String}
     */
    public static String toString(final Object t) {
        if (isEmptyIfStr(t)) {
            return null;
        }

        // 兼容 Optional
        if (t instanceof Optional<?>) {
            return toString(((Optional<?>) t).orElse(null));
        }

        return t.toString();
    }

    /**
     * 转为字符串，如果是{@code null},转为{@code ""},而不是{@code null}
     *
     * @param t t
     *
     * @return {@link String}
     */
    public static String toBlankString(final Object t) {
        return nullToEmpty(toString(t));
    }

    /**
     * 包含空白字符，或者是null的字符串
     *
     * @param str str
     *
     * @return boolean
     */
    public static boolean isNullBlank(final CharSequence str) {
        return isBlank(str) || NULL.contentEquals(str);
    }

    /**
     * 检查字符串是否为null、“null”、“undefined”
     *
     * @param str 被检查的字符串
     *
     * @return 是否为null、“null”、“undefined”
     *
     * @since 4.0.10
     */
    public static boolean isNullOrUndefined(CharSequence str) {
        if (null == str) {
            return true;
        }

        final String strString = str.toString().trim();
        return NULL.equalsIgnoreCase(strString) || "undefined".equalsIgnoreCase(strString);
    }

    /**
     * 是否是空白字符或者是非法输入字符串
     * <p>{@code null}</p>
     * <p>{@code NULL}</p>
     * <p>{@code Null}</p>
     * <p>{@code undefined}</p>
     * <p>{@code Undefined}</p>
     * <p>{@code UNDEFINED}</p>
     * <p>{@code [object Null]}</p>
     * <p>{@code [object null]}</p>
     * <p>{@code [OBJECT NULL]}</p>
     * <p>{@code object Null}</p>
     * <p>{@code object null}</p>
     * <p>{@code OBJECT NULL}</p>
     * <p>{@code Object Null}</p>
     *
     * @param str str
     *
     * @return boolean
     */
    public static boolean isBlankOrIllegalInputStr(CharSequence str) {
        if (null == str) {
            return true;
        }
        final String strString = str.toString().trim();
        // 过滤空格，\t \n等
        return isBlank(strString) ||
            // 过滤 null undefined等
            isNullOrUndefined(str)
            // fix: 前端 [object Null] 过滤
            || "[object Null]".equalsIgnoreCase(strString) || "object Null".equalsIgnoreCase(strString);
    }

    /**
     * 第一个字符是否大写
     *
     * @param str str
     *
     * @return boolean
     */
    public static boolean isUpperFirst(CharSequence str) {
        if (null == str) {
            return false;
        }

        return str.length() > 0 && Character.isUpperCase(str.charAt(0));
    }

    /**
     * 查询字符，不会越界，安全的查询方式
     *
     * @param str   str
     * @param index 指数
     *
     * @return {@link Character}
     */
    public static Character charAt(final String str, final int index) {
        if (StringUtil.isBlank(str)) {
            return null;
        }

        if (index < 0 || index >= str.length()) {
            return null;
        }

        return str.charAt(index);
    }

    /**
     * 第一个字符
     *
     * @param str str
     *
     * @return {@link Character}
     */
    public static Character firstChar(final String str) {
        return charAt(str, 0);
    }

    public static String firstCharAsString(final String str) {
        return StringUtil.toString(charAt(str, 0));
    }

    /**
     * 合并,返回第一个{@link #isNotBlank(CharSequence)}命中的值
     *
     * @param argsSupplier args供应商
     *
     * @return {@link T}
     */
    @SafeVarargs
    public static <T extends CharSequence> T coalesce(final Supplier<T>... argsSupplier) {
        if (ArrayUtil.isEmpty(argsSupplier)) {
            return null;
        }

        for (Supplier<T> supplier : argsSupplier) {
            final T arg = supplier.get();
            if (isNotBlank(arg)) {
                return arg;
            }
        }

        return null;
    }

    /**
     * 连接一批字符串，只要有一个为{@code null},最终返回{@code null}
     *
     * @param strs str
     *
     * @return {@link String}
     */
    public static String concat(CharSequence... strs) {
        final StrBuilder sb = new StrBuilder();
        for (CharSequence str : strs) {
            if (null == str) {
                return null;
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 转为全大写，避免空指针
     *
     * @param str str
     *
     * @return {@link String}
     */
    public static String toUpperCase(String str) {
        return null == str ? null : str.toUpperCase();
    }

    /**
     * 转为全小写，避免空指针
     *
     * @param str str
     *
     * @return {@link String}
     */
    public static String toLowerCase(String str) {
        return null == str ? null : str.toLowerCase();
    }

    /**
     * 如果{@code a}不为空，则返回{@code a},否则返回{@code b}
     *
     * @param a 一个
     * @param b b
     *
     * @return {@link String}
     */
    public static String orElse(final String a, String b) {
        return ObjectUtil.orElse(a, StringUtil::isNotBlank, b);
    }

    /**
     * 如果{@code a}不为空，则返回{@code a},否则调用{@code b}的值
     *
     * @param a 一个
     * @param b b
     *
     * @return {@link String}
     */
    public static String orElse(final String a, Supplier<String> b) {
        return ObjectUtil.orElse(a, StringUtil::isNotBlank, b);
    }

    /**
     * 字符串平分，如果是偶数，直接平分，如果是奇数，去掉最中间的元素后平分
     *
     * @param str str
     *
     * @return {@link Pair}<{@link String},{@link String}>
     */
    public static String[] splitEvenly(final String str) {
        final String[] array = new String[2];
        if (isBlank(str) || length(str) == 1) {
            array[0] = str;
            return array;
        }

        int len = str.length();
        int mid = len / 2;
        array[0] = str.substring(0, mid);
        array[1] = str.substring(NumberUtil.isOdd(len) ? mid + 1 : mid);
        return array;
    }

    /**
     * 编码字节数组为字符串，使用支持的编码
     *
     * @param bytes   字节
     * @param charset 字符集
     *
     * @return {@link String}
     */
    public static String newString(byte[] bytes, Charset charset) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        final Charset toCharset = ObjectUtil.orElse(charset, StandardCharsets.UTF_8);
        return new String(bytes, toCharset);
    }

    /**
     * 创建Utf8的字符串
     *
     * @param bytes 字节
     *
     * @return {@link String}
     */
    public static String newUtf8String(byte[] bytes) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 如果存在附加
     * 对象{@code o}不为空，则追加
     *
     * @param builder 构建器
     * @param o       o
     *
     * @return {@link StringBuilder}
     */
    public static StringBuilder appendIfPresent(StringBuilder builder, Object o) {
        return ObjectUtil.isNotEmpty(o) ? builder.append(o) : builder;
    }

    /**
     * 附加到末尾，如果存在
     *
     * @param builder 构建器
     * @param o       o
     * @param end     结束
     *
     * @return {@link StringBuilder}
     */
    public static StringBuilder appendEndIfPresent(StringBuilder builder, Object o, Object end) {
        if (ObjectUtil.isEmpty(o)) {
            return builder;
        }
        return appendIfPresent(builder.append(o), end);
    }

    /**
     * 附加到开头如果存在
     *
     * @param builder 构建器
     * @param start   开始
     * @param o       o
     *
     * @return {@link StringBuilder}
     */
    public static StringBuilder appendStartIfPresent(StringBuilder builder, Object start, Object o) {
        if (ObjectUtil.isEmpty(o)) {
            return builder;
        }
        if (ObjectUtil.isNotEmpty(start)) {
            builder.append(start);
        }

        return builder.append(o);
    }

    /**
     * 按照{@code regex}切割后查找第一个
     *
     * @param str   str
     * @param regex 正则表达式
     *
     * @return {@link String}
     */
    public static String splitAndFindFirst(String str, String regex) {
        return isBlank(str) ? null : ArrayUtil.findFirst(str.split(regex));
    }

    /**
     * 分割并查找指定位置的部分
     *
     * @param str   str
     * @param regex 正则表达式
     * @param index 位置
     *
     * @return {@link String}
     */
    public static String splitAndFind(String str, String regex, int index) {
        return isBlank(str) ? null : CollectionUtil.get(split(str, regex), index);
    }

    /**
     * 将版本号转为数字
     * 2.27.0   -> 2270
     * 2.27.0-1 -> 22701
     * 2.27.0-1anv  -> 22701
     * 2.27.0-1anv-beta -> 22701
     * release-2.27.0-1anv-beta -> 22701
     *
     * @param version 版本
     *
     * @return long
     */
    public static long toVersionNumber(String version) {
        if (isBlank(version)) {
            return 0;
        }

        String numberStringVersion = version.replaceAll("[a-zA-Z.-]", "");
        return NumberUtil.toPrimitiveLong(numberStringVersion);
    }

    /**
     * 分割为{@link Entry}
     *
     * @param charSequence 字符序列
     * @param separator    分隔符
     * @param ignored      忽略了
     *
     * @return {@code Entry<String, String>}
     */
    public static Entry<String, String> splitToEntry(String charSequence, String separator, boolean ignored) {
        if (isBlank(charSequence)) {
            return null;
        }

        final List<String> segments = splitTrim(charSequence, separator);


        return MapUtil.newImmutableEntry(CollectionUtil.getFirst(segments), CollectionUtil.get(segments, 1));
    }

    /**
     * 左对齐字符串，如果{@code str}小于{@code minLen}，则用{@code filledChar}在左侧填充，如果大于{@code maxLen},则截掉左侧大于的字符，生于情况返回原字符串
     *
     * @param str        str
     * @param filledChar 填充炭
     * @param minLen     最小
     * @param maxLen     仅最大
     *
     * @return {@link String}
     */
    public static String leftAlign(String str, char filledChar, int minLen, int maxLen) {
        int length = length(str);
        if (length < minLen) {
            return fillBefore(str, filledChar, minLen);
        }

        if (length > maxLen) {
            return subPre(str, length - maxLen);
        }

        // 介于中间
        return str;
    }

    /**
     * 当给定字符串为空时，转换为{@code null}
     *
     * @param str 被转换的字符串
     *
     * @return 转换后的字符串
     */
    public static String blankToNull(CharSequence str) {
        return isBlank(str) ? null : str.toString();
    }
}
