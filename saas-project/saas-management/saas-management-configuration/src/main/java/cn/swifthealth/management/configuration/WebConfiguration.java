package cn.swifthealth.management.configuration;

import cn.swifthealth.common.tools.ClassUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p> Web相关的配置 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {


    /**
     * 为每一个模块添加路径前缀
     *
     * @param configurer 配置
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/manage", c -> ClassUtil.belongTo(c, "cn.swifthealth.management.manage"));
    }

}
