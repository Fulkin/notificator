package com.notificator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(basePackages = "com.notificator.configuration",
        basePackageClasses = {ThreadPoolTaskSchedulerExamples.class})
public class ThreadPoolTaskSchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    public CronTrigger cronTrigger() {
        return new CronTrigger("10 * * * * ?");
    }

    /*@Bean
    public PeriodicTrigger periodicTrigger() {
        return new PeriodicTrigger(30, TimeUnit.SECONDS);
    }*/

    @Bean
    public PeriodicTrigger periodicFixedDelayTrigger() {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(30, TimeUnit.MINUTES);
        periodicTrigger.setFixedRate(true);
        periodicTrigger.setInitialDelay(1);
        return periodicTrigger;
    }
}