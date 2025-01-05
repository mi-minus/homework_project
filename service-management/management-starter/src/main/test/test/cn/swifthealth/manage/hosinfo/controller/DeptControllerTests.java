package test.cn.swifthealth.manage.hosinfo.controller;

import cn.swifthealth.management.manage.hosinfo.entity.BaseDeptInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * <p> 科室公共Controller </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes = cn.swifthealth.starter.ManagementStarter.class )
@AutoConfigureMockMvc
@Slf4j
public class DeptControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void findAllDeptByCond() throws Exception {
        mockMvc.perform(get("/manage/hos/dept/list")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void findAllDeptByCondWithDeptCode() throws Exception {
        mockMvc.perform(get("/manage/hos/dept/list")
                        .param("deptCode", "1")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void AddDept() throws Exception {
        String deptCode = UUID.randomUUID().toString();
        BaseDeptInfo baseDeptInfo = BaseDeptInfo.builder()
                .deptCode(deptCode)
                .deptName("测试科室" + deptCode)
                .state(1)
                .build();

        mockMvc.perform(post("/manage/hos/dept/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baseDeptInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(0))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void AddDeptWithDup() throws Exception {
        String deptCode = UUID.randomUUID().toString();
        BaseDeptInfo baseDeptInfo = BaseDeptInfo.builder()
                .deptCode(deptCode)
                .deptName("测试科室" + deptCode)
                .state(1)
                .build();

        mockMvc.perform(post("/manage/hos/dept/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(baseDeptInfo)));

        mockMvc.perform(post("/manage/hos/dept/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baseDeptInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(-1))
                .andExpect(jsonPath("message").value("请确认主键是否冲突"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void addDeptWithNull() throws Exception {
        BaseDeptInfo baseDeptInfo = new BaseDeptInfo();

        mockMvc.perform(post("/manage/hos/dept/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baseDeptInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(-1))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void updateDeptWithNoExist() throws Exception {
        String deptCode = UUID.randomUUID().toString();
        BaseDeptInfo baseDeptInfo = BaseDeptInfo.builder()
                .deptCode(deptCode)
                .deptName("测试科室" + deptCode)
                .state(1)
                .build();

        mockMvc.perform(post("/manage/hos/dept/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baseDeptInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(-1))
                .andExpect(jsonPath("message").value("需要更新的科室不存在"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void updateDeptWithExist() throws Exception {
        String deptCode = UUID.randomUUID().toString();
        BaseDeptInfo baseDeptInfo = BaseDeptInfo.builder()
                .deptCode(deptCode)
                .deptName("测试科室" + deptCode)
                .state(1)
                .build();

        mockMvc.perform(post("/manage/hos/dept/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(baseDeptInfo)));

        baseDeptInfo.setDeptName("updateName");
        mockMvc.perform(post("/manage/hos/dept/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baseDeptInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(0))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void updateDeptWithNull() throws Exception {
        BaseDeptInfo baseDeptInfo = new BaseDeptInfo();

        mockMvc.perform(post("/manage/hos/dept/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baseDeptInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(-1))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void deleteDeptWithExist() throws Exception {
        String deptCode = UUID.randomUUID().toString();
        BaseDeptInfo baseDeptInfo = BaseDeptInfo.builder()
                .deptCode(deptCode)
                .deptName("测试科室" + deptCode)
                .state(1)
                .build();

        mockMvc.perform(post("/manage/hos/dept/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(baseDeptInfo)));

        mockMvc.perform(delete("/manage/hos/dept/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("deptCode", deptCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(0))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void deleteDeptWithNoExist() throws Exception {
        String deptCode = UUID.randomUUID().toString();

        mockMvc.perform(delete("/manage/hos/dept/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("deptCode", deptCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(0))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void deleteDeptWithEmpty() throws Exception {
        mockMvc.perform(delete("/manage/hos/dept/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("deptCode", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(-1))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }
}
