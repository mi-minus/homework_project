package test.cn.swifthealth.manage.hosinfo.service;

import cn.swifthealth.management.manage.hosinfo.entity.BaseDeptInfo;
import cn.swifthealth.management.manage.hosinfo.service.DeptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


/**
 * <p> 测试用例 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = cn.swifthealth.starter.ManagementStarter.class)
public class DeptServiceTests {

    @Autowired
    private DeptService deptService;

    @Test
    public void findAllDeptByCond() {
        assertNotNull(deptService.findAllDeptByCond(null));
    }

    @Test
    public void testAddDept() {
        String deptCode = UUID.randomUUID().toString();
        BaseDeptInfo baseDeptInfo = BaseDeptInfo.builder()
                .deptCode(deptCode)
                .deptName("测试科室" + deptCode)
                .state(1)
                .build();
        assertTrue(deptService.addDeptInfo(baseDeptInfo));
    }

    @Test
    public void testUpdateDept() {
        String deptCode = UUID.randomUUID().toString();
        BaseDeptInfo baseDeptInfo = BaseDeptInfo.builder()
                .deptCode(deptCode)
                .deptName("测试科室" + deptCode)
                .state(1)
                .build();
        assertTrue(deptService.addDeptInfo(baseDeptInfo));

        baseDeptInfo.setDeptName("测试科室");
        assertTrue(deptService.updateDeptInfo(baseDeptInfo));

        List<BaseDeptInfo> baseDeptInfo1 = deptService.findAllDeptByCond(deptCode);
        assertTrue(baseDeptInfo1.size() > 0);
        assertEquals(baseDeptInfo1.get(0).getDeptName(), "测试科室");
    }

    @Test
    public void testDelDept() {
        String deptCode = UUID.randomUUID().toString();
        BaseDeptInfo baseDeptInfo = BaseDeptInfo.builder()
                .deptCode(deptCode)
                .deptName("测试科室" + deptCode)
                .state(1)
                .build();
        assertTrue(deptService.addDeptInfo(baseDeptInfo));
        assertTrue(deptService.delDeptInfo(deptCode));
        List<BaseDeptInfo> baseDeptInfo1 = deptService.findAllDeptByCond(deptCode);
        assertTrue(baseDeptInfo1.size() == 0);
    }
}
