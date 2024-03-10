package com.example.demo.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    //JobRepository(배치 작업 중의 정보를 저장하는 저장소 역할)를 통해서 JobExecution을 생성함
    //JobExecution은 PlatformTranscationManager를 이용하여 Transcation 처리함
    //step 실행 시에도 PlatformTranscationManger를 이용하여 Transcation 처리함

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
                .start(step())
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .build();
    }
    
    @Bean
    public Step step() {
        return new StepBuilder("step", jobRepository)
                .tasklet(firstTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet firstTasklet() {
        return ((contribution, chunkContext) -> {
            //tasklet은 대용량일 때 적당하지 않음
            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            String [] skin = {"adopt","basic","faq","qna","storage"};
            for (String s : skin) {
                // boardGroupRepository.updateTopDate("board_"+s);
            }
            return RepeatStatus.FINISHED;
        });
    }
}
