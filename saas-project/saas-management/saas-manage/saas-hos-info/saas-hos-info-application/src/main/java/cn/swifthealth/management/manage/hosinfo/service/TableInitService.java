package cn.swifthealth.management.manage.hosinfo.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class TableInitService implements CommandLineRunner {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void run(String... args) {
        threadPoolTaskExecutor.execute(() -> {
            String sql = "create schema test;\n" +
                    "create table test.base_dept_info (\n" +
                    "    dept_code varchar2 PRIMARY KEY ,\n" +
                    "    dept_name varchar2,\n" +
                    "    dept_type varchar2,\n" +
                    "    introduction text,\n" +
                    "    special text,\n" +
                    "    state smallint )";
            jdbcTemplate.execute(sql);
            log.info("初始化建表成功");
        });

    }
}
