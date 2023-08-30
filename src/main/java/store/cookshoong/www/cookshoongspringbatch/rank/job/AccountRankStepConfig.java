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
import store.cookshoong.www.cookshoongspringbatch.logging.rank.RankInfoListener;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountOrderDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.UpdateAccountRankDto;
import store.cookshoong.www.cookshoongspringbatch.rank.processor.AccountRankProcessor;
import store.cookshoong.www.cookshoongspringbatch.rank.reader.AccountRankReader;
import store.cookshoong.www.cookshoongspringbatch.rank.writer.AccountRankUpdateWriter;

/**
 * 회원 등급 갱신하는 Step.
 *
 * @author seungyeon (유승연)
 * @since 2023.08.01
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountRankStepConfig {
    private static final Integer CHUNK_SIZE = 5000;
    private final StepBuilderFactory stepBuilderFactory;
    private final AccountRankReader rankReader;
    private final AccountRankUpdateWriter rankWriter;
    private final AccountRankProcessor rankProcessor;
    private final LoggingListener loggingListener;
    private final RankInfoListener rankInfoListener;

    /**
     * 회원 등급 갱신 - 회원아이디, 주문횟수를 읽어오고, 기준에 맞추어 재산정 후, 등급 변경.
     *
     * @return the step
     */
    @JobScope
    @Bean
    public Step changeRankStep() {
        log.info("등급 재산정 스텝 시작!");
        return stepBuilderFactory.get("등급 재산정")
            .<SelectAccountOrderDto, UpdateAccountRankDto>chunk(CHUNK_SIZE)
            .reader(rankReader.accountRankRead(null, null))
            .processor(rankProcessor)
            .writer(rankWriter.updateAccountRank())
            .listener(loggingListener)
            .listener(rankInfoListener)
            .faultTolerant()
            .retry(ConnectTimeoutException.class)
            .retry(DeadlockLoserDataAccessException.class)
            .noRetry(SQLException.class)
            .noSkip(SQLException.class)
            .retryLimit(3)
            .build();

    }
}
