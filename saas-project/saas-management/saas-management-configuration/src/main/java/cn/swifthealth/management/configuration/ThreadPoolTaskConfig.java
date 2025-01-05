package cn.swifthealth.management.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 *
 */
@Slf4j
@Configuration
public class ThreadPoolTaskConfig {
    /**
     * 最少线程数
     * 当池子大小小于corePoolSize就新建线程，并处理请求
     */
    @Value("${threadPool.corePoolSize:20}")
    private Integer corePoolSize;

    /**
     * 允许线程闲置时间,单位：秒
     * 当池子的线程数大于corePoolSize的时候，多余的线程会等待keepAliveTime长的时间，如果无请求可处理就自行销毁
     */
    @Value("${threadPool.keepAliveSeconds:3000}")
    private Integer keepAliveSeconds;

    /**
     * 最大线程数
     * 当workQueue放不下新入的任务时，新建线程入池，并处理请求
     * 如果池子大小撑到了maximumPoolSize就用RejectedExecutionHandler来做拒绝处理.
     */
    @Value("${threadPool.maxPoolSize:200}")
    private Integer maxPoolSize;

    /**
     * 线程池缓冲队列大小
     * 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去从workQueue中取任务并处理
     */
    @Value("${threadPool.queueCapacity:1000}")
    private Integer queueCapacity;

    private static final String LOG_PREFIX = "【线程池通知】";

    /**
     * 线程池初始化
     *
     * @return
     */
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        log.info("{}线程初始化开始，corePoolSize={} | keepAliveSeconds={} | maxPoolSize={} | queueCapacity={}", LOG_PREFIX,
                corePoolSize, keepAliveSeconds, maxPoolSize, queueCapacity);
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        log.info("{}线程初始化结束", LOG_PREFIX);
        return threadPoolTaskExecutor;
    }
}
