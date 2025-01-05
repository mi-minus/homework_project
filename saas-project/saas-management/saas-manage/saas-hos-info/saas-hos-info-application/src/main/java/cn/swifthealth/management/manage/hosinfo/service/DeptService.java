package cn.swifthealth.management.manage.hosinfo.service;

import java.util.ArrayList;
import java.util.List;

import cn.swifthealth.common.jsonRes.APIException;
import cn.swifthealth.common.jsonRes.ResponseMessageErrorCodeEnum;
import cn.swifthealth.common.tools.CollectionUtil;
import cn.swifthealth.common.tools.StringUtil;
import cn.swifthealth.common.validation.Assert;
import cn.swifthealth.management.manage.hosinfo.entity.BaseDeptInfo;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


/**
 * <p> 科室公共 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
@Service
@Slf4j
@AllArgsConstructor
public class DeptService {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final Cache<String, Object> caffeineCache;

    /**
     * 查询所有的科室
     */
    public List<BaseDeptInfo> findAllDeptByCond(String deptCode, Integer page, Integer size) {
        List<BaseDeptInfo> depts = new ArrayList<>();
        if (StringUtil.isNotBlank(deptCode)) {
            List<BaseDeptInfo> objects = (List<BaseDeptInfo>) caffeineCache.asMap().get(deptCode);
            if (CollectionUtil.isNotEmpty(objects)) {
                return objects;
            }
            try {
                String querySql = "select dept_code, dept_name, dept_type, introduction, special, state " +
                        "from test.base_dept_info where state=1 and dept_code='" + deptCode + "'";
                depts = jdbcTemplate.query(querySql, (rs, rowNum) -> new BaseDeptInfo(rs.getString("dept_code"), rs.getString("dept_name"), rs.getString("dept_type"), rs.getString("introduction"), rs.getString("special"), rs.getInt("state")));
                return depts;
            } catch (BadSqlGrammarException e) {
                throw new APIException("请确认是否sql是否正确或者建表成功(建表语句参考文档)");
            } catch (Exception e) {
                throw new APIException("其他异常请自行排查");
            }
        }

        List<BaseDeptInfo> objects = (List<BaseDeptInfo>) caffeineCache.asMap().get("deptInfo");
        if (CollectionUtil.isNotEmpty(objects)) {
            return objects;
        }

        try {
            depts = jdbcTemplate.query("select dept_code, dept_name, dept_type, introduction, special, state from test.base_dept_info " +
                    "where state=1 limit " + size + " offset " + page * size,
                    (rs, rowNum) -> new BaseDeptInfo(rs.getString("dept_code"), rs.getString("dept_name"), rs.getString("dept_type"), rs.getString("introduction"), rs.getString("special"), rs.getInt("state")));
        } catch (BadSqlGrammarException e) {
            throw new APIException("请确认是否sql是否正确或者建表成功(建表语句参考文档)");
        } catch (Exception e) {
            throw new APIException("其他异常请自行排查");
        }
        caffeineCache.put("deptInfo", depts);
        log.info("call the interface");
        return depts;
    }

    /**
     * 新增科室
     */
    public boolean addDeptInfo(BaseDeptInfo deptInfo) {
        Assert.isNotEmpty(deptInfo.getDeptCode(), () -> {
            throw new APIException("科室编码不可为空");
        });
        String sql = "insert into test.base_dept_info (dept_code, dept_name, dept_type, introduction, special, state)" + "values (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, deptInfo.getDeptCode(), deptInfo.getDeptName(), deptInfo.getDeptType(), deptInfo.getIntroduction(), deptInfo.getSpecial(), 1);
            caffeineCache.cleanUp();
            return true;
        } catch (BadSqlGrammarException e) {
            throw new APIException("请确认是否sql是否正确或者建表成功(建表语句参考文档)");
        } catch (DuplicateKeyException e) {
            throw new APIException("请确认主键是否冲突");
        } catch (Exception e) {
            throw new APIException("其他异常请自行排查");
        }
    }

    /**
     * 删除科室
     */
    public boolean updateDeptInfo(BaseDeptInfo deptInfo) {
        Assert.isNotNull(deptInfo, () -> {
            throw new APIException("入参不可为空");
        });
        Assert.isNotEmpty(deptInfo.getDeptCode(), () -> {
            throw new APIException("科室编码不可为空");
        });
        String querySql = "select count(1) as cnt from test.base_dept_info where dept_code='" + deptInfo.getDeptCode() + "'";
        Integer cnt = jdbcTemplate.queryForObject(querySql, (rs, rowNum) -> rs.getInt("cnt"));
        log.info("query-cnt: {}", cnt);
        if (cnt <= 0) {
            throw new APIException("需要更新的科室不存在");
        }

        try {
            String sql = "update test.base_dept_info set dept_name=?, dept_type=?,introduction=?,special=?, state=? where dept_code=? ";
            jdbcTemplate.update(sql, deptInfo.getDeptName(), deptInfo.getDeptType(), deptInfo.getIntroduction(), deptInfo.getSpecial(), deptInfo.getState(), deptInfo.getDeptCode());
            caffeineCache.cleanUp();
            return true;
        } catch (BadSqlGrammarException e) {
            throw new APIException("请确认是否sql是否正确或者建表成功(建表语句参考文档)");
        } catch (Exception e) {
            throw new APIException("其他异常请自行排查");
        }
    }

    /**
     * 删除科室
     */
    public boolean delDeptInfo(String deptCode) {
        Assert.isNotEmpty(deptCode, () -> {throw new APIException("科室编码不可为空");});
        try {
            String sql = "delete from test.base_dept_info where dept_code=?";
            jdbcTemplate.update(sql, deptCode);
            caffeineCache.cleanUp();
            return true;
        } catch (BadSqlGrammarException e) {
            throw new APIException("请确认是否sql是否正确或者建表成功(建表语句参考文档)");
        } catch (Exception e) {
            throw new APIException("其他异常请自行排查");
        }
    }
}
