package com.zonbeozon.post.service.recommend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScoreUpdateScheduler {
    @Qualifier("scoreUpdateJob")
    private final Job scoreUpdateJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 */15 * * * *")
    public void runScoreUpdateJob() {
        try {
            log.info("스코어 업데이트 시작");
            JobParametersBuilder paramsBuilder = new JobParametersBuilder();
            paramsBuilder.addLong("run.time", System.currentTimeMillis());
            jobLauncher.run(scoreUpdateJob, paramsBuilder.toJobParameters());
            log.info("스코어 업데이트 완료");
        } catch (Exception e) {
            log.error("Post 스코어 업데이트 중 오류 발생", e);
        }
    }
}
