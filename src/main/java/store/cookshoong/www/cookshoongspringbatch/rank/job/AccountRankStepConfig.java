package store.cookshoong.www.cookshoongspringbatch.rank.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.logging.LoggingListener;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountOrderDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.UpdateAccountRankDto;
import store.cookshoong.www.cookshoongspringbatch.rank.processor.AccountRankProcessor;
import store.cookshoong.www.cookshoongspringbatch.rank.reader.AccountRankReader;
import store.cookshoong.www.cookshoongspringbatch.rank.writer.CompositeRankWriter;

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
    private static final Integer CHUNK_SIZE = 10;
    private final StepBuilderFactory stepBuilderFactory;
    private final AccountRankReader rankReader;
    private final CompositeRankWriter rankWriter;
    private final AccountRankProcessor rankProcessor;
    private final LoggingListener loggingListener;

    /**
     * 회원 등급 갱신 - 회원아이디, 주문횟수를 읽어오고, 기준에 맞추어 재산정 후, 등급 변경.
     *
     * @return the step
     */
    @JobScope
    @Bean
    public Step changeRankStep() {
        return stepBuilderFactory.get("등급 재산정")
            .allowStartIfComplete(true)
            .<SelectAccountOrderDto, UpdateAccountRankDto>chunk(CHUNK_SIZE)
            .reader(rankReader.accountRankRead())
            .processor(rankProcessor)
            .writer(rankWriter.compositeRankAndCouponWriter())
            .listener(loggingListener)
            .build();

    }
}
