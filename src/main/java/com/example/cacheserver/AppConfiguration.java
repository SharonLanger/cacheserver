package com.example.cacheserver;

import com.example.cacheserver.service.ExecutorService;
import com.example.cacheserver.service.ThreadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfiguration {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(100);
        executor.setCorePoolSize(1);
        executor.setThreadNamePrefix("my_task_executor_thread");
        executor.initialize();
        return executor;
    }

//    @Bean
//    public ExecutorService executorService() {
//        return new ExecutorService();
//    }
//
//    @Bean
//    public ThreadService threadService() {
//        return new ThreadService()
//                .setExecutorService(new ExecutorService());
//    }


}
