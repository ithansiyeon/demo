package com.example.demo.batch.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class MyTasklet implements Tasklet {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 업데이트할 데이터를 조회하고 처리하는 로직을 작성
        MyEntity entity = entityManager.find(MyEntity.class, 1L); // 예시로 ID가 1인 엔티티 조회
        if (entity != null) {
            entity.setDescription("Updated description"); // 엔티티의 내용을 업데이트
            entityManager.merge(entity); // 엔티티를 저장
            entityManager.flush(); // 변경 사항을 DB에 반영
        }

        System.out.println("MyTasklet is executing!");

        return RepeatStatus.FINISHED;
    }
}
