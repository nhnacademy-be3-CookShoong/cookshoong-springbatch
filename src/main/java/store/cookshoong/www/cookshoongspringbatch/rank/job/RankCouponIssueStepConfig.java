package store.cookshoong.www.cookshoongspringbatch.rank.job;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;
import store.cookshoong.www.cookshoongspringbatch.logging.LoggingListener;
import store.cookshoong.www.cookshoongspringbatch.logging.rank.CustomChunkListener;
import store.cookshoong.www.cookshoongspringbatch.logging.rank.RankIssueListener;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.InsertRankCouponDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountRankCouponDto;
import store.cookshoong.www.cookshoongspringbatch.rank.processor.RankCouponProcessor;
import store.cookshoong.www.cookshoongspringbatch.rank.reader.RankCouponReader;
import store.cookshoong.www.cookshoongspringbatch.rank.writer.RankCouponWriter;

/**
 * 등급 쿠폰 발급 스텝.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RankCouponIssueStepConfig {
    private static final Integer CHUNK_SIZE = 10000;
    private final StepBuilderFactory stepBuilderFactory;
    private final LoggingListener loggingListener;
    private final RankCouponReader rankCouponReader;
    private final RankCouponProcessor rankCouponProcessor;
    private final RankCouponWriter rankCouponWriter;
    private final RankIssueListener rankIssueListener;
    private final CustomChunkListener customChunkListener;

    /**
     * Change rank step step.
     *
     * @return the step
     */
    @JobScope
    @Bean
    public Step issueRankCouponStep() {
        log.info("등급 쿠폰 발급 스텝 시작!");
        return stepBuilderFactory.get("등급쿠폰 발급")
            .<SelectAccountRankCouponDto, InsertRankCouponDto>chunk(CHUNK_SIZE)
            .reader(rankCouponReader.rankCouponRead())
            .processor(rankCouponProcessor)
            .writer(rankCouponWriter.insertRankCouponWriter())
            .listener(loggingListener)
            .listener(customChunkListener)
            .listener(rankIssueListener)
            .faultTolerant()
            .retry(ConnectTimeoutException.class)
            .retry(DeadlockLoserDataAccessException.class)
            .retryLimit(3)
            .skip(SQLException.class)
            .skipLimit(0)
            .build();
    }
}
