package com.example.demo.batch.config;

import org.quartz.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

   /* @Bean
    public Tasklet myTasklet() {
        return new MyTasklet();
    }

    @Bean
    public Step myStep(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("myStep", jobRepository)
                .tasklet(myTasklet, platformTransactionManager)
                .build();
    }*/
    // 이전의 코드를 유지하고, 아래 부분만 수정합니다.

    @Bean
    public JobDetail sampleJobDetail() {
        return JobBuilder.newJob(SampleJob.class)
                .withIdentity("sampleJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger sampleJobTrigger(JobDetail sampleJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(sampleJobDetail)
                .withIdentity("sampleTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?")) // 매시 정각에 실행
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger sampleJobTrigger) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setTriggers(sampleJobTrigger);
        return schedulerFactory;
    }
}

