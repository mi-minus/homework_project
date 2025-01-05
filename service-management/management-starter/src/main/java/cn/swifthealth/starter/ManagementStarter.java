package cn.swifthealth.starter;

import cn.swifthealth.management.application.ManagementApplication;
import org.springframework.boot.SpringApplication;

/**
 * <p> Management模块启动类 </p>
 * <p>创建于 2023/9/7 上午9:48 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
@ManagementApplication
public class ManagementStarter extends SpringApplication {

    public static void main(String[] args) {
        run(ManagementStarter.class, args);
    }
}
