package com.zonbeozon.post.service.recommend;

import com.zonbeozon.global.QuerydslPagingItemReader;
import com.zonbeozon.post.domain.PostMetric;
import com.zonbeozon.post.dto.GlobalScoreStats;
import com.zonbeozon.post.dto.SubScoreDto;
import com.zonbeozon.post.dto.TotalScoreDto;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final SimplePostContentScoreCalculator postContentAccessor;
    private final SubScoreCalculator subScoreCalculator;
    private final DataSource dataSource;
    private final JobExplorer jobExplorer;

    @Bean
    public Job scoreUpdateJob(
            @Qualifier("subScoreUpdateStep") Step subScoreUpdateStep,
            @Qualifier("totalScoreUpdateStep") Step totalScoreUpdateStep
    ) {
        return new JobBuilder("scoreUpdateJob", jobRepository)
                .start(subScoreUpdateStep)
                .next(statisticsCalcuateStep())
                .next(totalScoreUpdateStep)
                .build();
    }

    @Bean
    public Step subScoreUpdateStep(
            QuerydslPagingItemReader<PostMetric> postMetricReader,
            ItemProcessor<PostMetric, SubScoreDto> scoreCalculateProcessor,
            ItemWriter<SubScoreDto> scoreWriter

    ) {
        return new StepBuilder("scoreUpdateStep", jobRepository)
                .<PostMetric, SubScoreDto>chunk(CHUNK_SIZE, transactionManager) // <Reader반환타입, Writer입력타입>
                .reader(postMetricReader)
                .processor(scoreCalculateProcessor)
                .writer(scoreWriter)
                .build();
    }

    @Bean
    public ItemProcessor<PostMetric, SubScoreDto> ScoreCalculateProcessor() {
        return item -> {
            double contentScore = postContentAccessor.calculate(item.getPost().getContent());
            double engagementScore = subScoreCalculator.calculateEngagementScore(item);
            double qualityScore = subScoreCalculator.calculateQualityScore(item);
            double popularityScore = subScoreCalculator.calculatePopularityScore(item);
            return new SubScoreDto(item.getId(), contentScore, engagementScore, qualityScore, popularityScore);
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
    public ItemWriter<SubScoreDto> scoreWriter() {
        return new JdbcBatchItemWriterBuilder<SubScoreDto>()
                .dataSource(dataSource)
                .sql("""
                    UPDATE post_metric SET
                        content_score = :contentScore,
                        engagement_score = :engagementScore,
                        quality_score = :qualityScore,
                        popularity_score = :popularityScore
                    WHERE id = :postMetricId
                    """)
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

    @Bean
    public Step statisticsCalcuateStep() {
        return new StepBuilder("statisticsStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                    String sql = """
                        SELECT 
                            AVG(content_score) as avg_cont, STDDEV(content_score) as std_cont,
                            AVG(engagement_score) as avg_eng, STDDEV(engagement_score) as std_eng,
                            AVG(quality_score) as avg_qual, STDDEV(quality_score) as std_qual,
                            AVG(popularity_score) as avg_pop, STDDEV(popularity_score) as std_pop
                        FROM post_metric
                    """;

                    GlobalScoreStats stats = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new GlobalScoreStats(
                            rs.getDouble("avg_cont"), rs.getDouble("std_cont"),
                            rs.getDouble("avg_eng"), rs.getDouble("std_eng"),
                            rs.getDouble("avg_qual"), rs.getDouble("std_qual"),
                            rs.getDouble("avg_pop"), rs.getDouble("std_pop")
                    ));

                    ExecutionContext jobContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
                    jobContext.put("GLOBAL_STATS", stats);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step totalScoreUpdateStep(
            QuerydslPagingItemReader<PostMetric> postMetricReader,
            @Qualifier("totalScoreProcessor") ItemProcessor<PostMetric, TotalScoreDto> totalScoreProcessor,
            @Qualifier("totalScoreWriter") ItemWriter<TotalScoreDto> totalScoreWriter
    ) {
        return new StepBuilder("totalScoreStep", jobRepository)
                .<PostMetric, TotalScoreDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(postMetricReader)
                .processor(totalScoreProcessor)
                .writer(totalScoreWriter)
                .build();
    }

    @Bean
    @StepScope // JobExecutionContext를 주입받으려면 필수
    public ItemProcessor<PostMetric, TotalScoreDto> totalScoreProcessor(
            @Value("#{jobExecutionContext['GLOBAL_STATS']}") GlobalScoreStats stats
    ) {
        TotalScoreCalculator totalScoreCalculator = new TotalScoreCalculator(stats);
        return totalScoreCalculator::calculate;
    }

    @Bean
    public ItemWriter<TotalScoreDto> totalScoreWriter() {
        return new JdbcBatchItemWriterBuilder<TotalScoreDto>()
                .dataSource(dataSource)
                .sql("UPDATE post_metric SET total_score = :totalScore WHERE id = :id")
                .beanMapped()
                .build();
    }
}
