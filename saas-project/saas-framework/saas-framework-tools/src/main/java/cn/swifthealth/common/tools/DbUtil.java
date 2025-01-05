package cn.swifthealth.common.tools;

import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.sql.SqlExecutor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p> 数据库相关的工具类 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 2.26.6
 */
public class DbUtil {

    /**
     * 最简单的SQL {@code SELECT 1 AS a}
     */
    public static final String SQL_SIMPLE_SELECT = "SELECT 1 AS a";

    /**
     * 对于SQL: {@code SELECT 1 AS a FROM xxx LIMIT 1}返回结果的常量
     */
    public static final int EXIST_LINE_RESULT = 1;

    /**
     * 数据库中存在至少一行数据
     *
     * @param line 行
     *
     * @return boolean
     */
    public static boolean existLine(Number line) {
        // 返回值不为空，且行数大于0
        return null != line && line.longValue() > 0;
    }

    /**
     * 数据库中存在至少一行数据，用{@code SELECT 1::BOOLEAN AS a FROM xxx LIMIT 1}来判断
     *
     * @param result 结果
     *
     * @return boolean
     */
    public static boolean existLine(Boolean result) {
        // 返回值不为空，且为true
        return BooleanUtil.isTrue(result);
    }

    /**
     * 数据库中存在至少一行数据
     *
     * @param line 行
     *
     * @return boolean
     */
    public static boolean unExistLine(Number line) {
        // 返回值不为空，且行数大于0
        return !existLine(line);
    }

    /**
     * 存在行判断，断言{@code result}必须等于{@code value},适用于高性能SQL: {@code SELECT 1 AS a FROM xxx LIMIT 1}
     *
     * @param result 后果
     * @param value  数字
     *
     * @return boolean
     */
    public static boolean assertLine(Number result, Number value) {
        return NumberUtil.isEqual(result, value);
    }

    /**
     * 存在行判断
     *
     * @param result 后果
     *
     * @return boolean
     *
     * @see #assertLine(Number, Number)
     */
    public static boolean assertLine(Number result) {
        return assertLine(result, EXIST_LINE_RESULT);
    }

    /**
     * 有效连接
     *
     * @param connection 连接
     *
     * @return boolean
     */
    public static boolean validConnection(Connection connection) {
        try {
            final Number number = SqlExecutor.query(connection, SQL_SIMPLE_SELECT, NumberHandler.create());
            return NumberUtil.equals(number, 1);
        } catch (SQLException e) {
            return false;
        }
    }
}
