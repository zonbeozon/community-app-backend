package com.zonbeozon.post.service.metric;

import com.zonbeozon.global.QuerydslPagingItemReader;
import com.zonbeozon.post.domain.PostMetric;
import com.zonbeozon.post.dto.PostMetricScoreDto;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static com.zonbeozon.post.domain.QPostMetric.postMetric;

@Configuration
@RequiredArgsConstructor
public class ScoreUpdateConfig {
    private static final int CHUNK_SIZE = 200;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final SimplePostContentAccessor postContentAccessor;
    private final PostScoreAssessor postScoreAssessor;
    private final DataSource dataSource;
    private final JobExplorer jobExplorer;

    @Bean
    public Job scoreUpdateJob(
            @Qualifier("scoreUpdateStep") Step scoreUpdateStep
    ) {
        return new JobBuilder("scoreUpdateJob", jobRepository)
                .start(scoreUpdateStep)
                .build();
    }

    @Bean
    public Step scoreUpdateStep(
            QuerydslPagingItemReader<PostMetric> postMetricReader,
            ItemProcessor<PostMetric, PostMetricScoreDto> scoreCalculateProcessor,
            ItemWriter<PostMetricScoreDto> scoreWriter

    ) {
        return new StepBuilder("scoreUpdateStep", jobRepository)
                .<PostMetric, PostMetricScoreDto>chunk(CHUNK_SIZE, transactionManager) // <Reader반환타입, Writer입력타입>
                .reader(postMetricReader)
                .processor(scoreCalculateProcessor)
                .writer(scoreWriter)
                .build();
    }

    @Bean
    public ItemProcessor<PostMetric, PostMetricScoreDto> ScoreCalculateProcessor() {
        return item -> {
            double contentScore = postContentAccessor.assess(item.getPost().getContent());
            double totalScore = postScoreAssessor.assess(item, contentScore);
            return new PostMetricScoreDto(item.getId(), contentScore, totalScore);
        };
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<PostMetric> postMetricReader() {
        return new QuerydslPagingItemReader<>(entityManagerFactory, CHUNK_SIZE, (queryFactory) ->
                queryFactory.selectFrom(postMetric)
                    .join(postMetric.post).fetchJoin()
                    .orderBy(postMetric.id.asc())
        );
    }

    @Bean
    public ItemWriter<PostMetricScoreDto> scoreWriter() {
        return new JdbcBatchItemWriterBuilder<PostMetricScoreDto>()
                .dataSource(dataSource)
                .sql("UPDATE post_metric SET content_score = :contentScore, total_score = :totalScore WHERE id = :postMetricId")
                .beanMapped()
                .build();
    }

    @Bean
    public LastScoreUpdatedTimeProvider lastScoreUpdatedTimeProvider() {
        return () -> {
            JobInstance lastJobInstance = jobExplorer.getLastJobInstance("scoreUpdateJob");
            if (lastJobInstance == null) {
                return null;
            }
            JobExecution lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);

            if (lastJobExecution != null && lastJobExecution.getStatus() == BatchStatus.COMPLETED) {
                return lastJobExecution.getEndTime();
            }
            return null;
        };
    }

}
