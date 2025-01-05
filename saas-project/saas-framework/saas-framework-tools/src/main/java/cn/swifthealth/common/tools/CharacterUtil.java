package cn.swifthealth.common.tools;

import cn.hutool.core.util.CharUtil;

/**
 * <p> 字符工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
public class CharacterUtil extends CharUtil {

    /**
     * 符号{@code *}
     */
    public static final char ASTERISK = '*';

    /**
     * 字符转为大写
     *
     * @param c c
     *
     * @return {@link Character}
     */
    public static Character toUpperCase(final Character c) {
        if (null == c) {
            return null;
        }
        return Character.isUpperCase(c) ? c : Character.toUpperCase(c);
    }

}
