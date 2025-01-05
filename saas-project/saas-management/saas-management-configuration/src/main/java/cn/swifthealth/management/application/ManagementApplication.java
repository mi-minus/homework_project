package cn.swifthealth.management.application;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> Management应用 </p>
 *
 * @author <a href="mailto:minus@swifthealth.cn">minus</a>
 * @version v1.0
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication(scanBasePackages = {
    "cn.swifthealth." + ManagementApplication.SERVICE_NAME + ".configuration",
    "cn.swifthealth." + ManagementApplication.SERVICE_NAME + ".**.controller",
    "cn.swifthealth." + ManagementApplication.SERVICE_NAME + ".**.service",
    "cn.swifthealth." + ManagementApplication.SERVICE_NAME + ".*.customize.impl",
    "cn.swifthealth.saasframework.**.component",
    "cn.swifthealth.saasframework.*.controller",
    "cn.swifthealth.saasframework.*.service",
    "cn.swifthealth.common.jsonRes",
    "cn.swifthealth.saasframework.config"}, exclude = {RedissonAutoConfigurationV2.class})
public @interface ManagementApplication {
    /**
     * 服务名称
     */
    String SERVICE_NAME = "management";
}
