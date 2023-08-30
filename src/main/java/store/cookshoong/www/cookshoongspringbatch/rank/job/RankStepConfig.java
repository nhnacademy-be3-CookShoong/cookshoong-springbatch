package store.cookshoong.www.cookshoongspringbatch.rank.job;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;
import store.cookshoong.www.cookshoongspringbatch.logging.LoggingListener;
import store.cookshoong.www.cookshoongspringbatch.logging.rank.AccountRankListener;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;
import store.cookshoong.www.cookshoongspringbatch.rank.reader.RanksReader;
import store.cookshoong.www.cookshoongspringbatch.rank.writer.RanksWriter;

/**
 * 등급을 가져오는 Step.
 *
 * @author seungyeon (유승연)
 * @since 2023.08.03
 */
@Configuration
@RequiredArgsConstructor
public class RankStepConfig {
    private static final Integer CHUNK_SIZE = 10;
    private final StepBuilderFactory stepBuilderFactory;
    private final LoggingListener loggingListener;
    private final RanksReader ranksReader;
    private final RanksWriter ranksWriter;
    private final AccountRankListener accountRankListener;

    /**
     * 등급에 대한 리스트 가져오는 Step.
     *
     * @return the step
     */
    @Bean
    @JobScope
    public Step getRankInfoStep() {
        return stepBuilderFactory.get("등급 이름들 가져오기")
            .allowStartIfComplete(true)
            .<RankDto, RankDto>chunk(CHUNK_SIZE)
            .reader(ranksReader.getRankCodes())
            .writer(ranksWriter)
            .faultTolerant()
            .retry(ConnectTimeoutException.class)
            .retry(DeadlockLoserDataAccessException.class)
            .noRetry(SQLException.class)
            .noSkip(SQLException.class)
            .retryLimit(3)
            .listener(promotionListener())
            .listener(loggingListener)
            .listener(accountRankListener)
            .build();
    }

    /**
     * StepExecution에서 JobExecution으로 승격하는 promotionListener.
     *
     * @return the execution context promotion listener
     */
    @JobScope
    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        executionContextPromotionListener.setKeys(new String[]{"rankCodes"});
        return executionContextPromotionListener;
    }
}
