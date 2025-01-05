package test.cn.swifthealth.manage.hosinfo.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private MockMvc mockMvc;

    @Test
    public void findAllDeptByCond() throws Exception {
        mockMvc.perform(get("/manage/hos/dept/list"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }
}
