package com.teamzero.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Multi Thread처리를 하기 위한 스케쥴러 Config
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

  @Value("${scheduler.thread.pool}")
  private int threadPool;
  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    ThreadPoolTaskScheduler threadPoolTaskScheduler =
        new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(threadPool);
    threadPoolTaskScheduler.initialize();
    taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
  }
}
