package store.cookshoong.www.cookshoongspringbatch.birthday.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.InsertIssueCouponDto;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.SelectAccountDto;
import store.cookshoong.www.cookshoongspringbatch.birthday.processor.BirthdayCouponIssueProcessor;
import store.cookshoong.www.cookshoongspringbatch.birthday.reader.BirthdayAccountReader;
import store.cookshoong.www.cookshoongspringbatch.birthday.writer.BirthdayAccountWriter;
import store.cookshoong.www.cookshoongspringbatch.logging.birthday.BirthdayCouponListener;
import store.cookshoong.www.cookshoongspringbatch.logging.birthday.BirthdayCouponSkipListener;
import store.cookshoong.www.cookshoongspringbatch.logging.LoggingListener;

/**
 * 생일쿠폰 발급 Step.
 *
 * @author seungyeon
 * @since 2023.08.05
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BirthdayCouponStepConfig {
    private static final Integer CHUNK_SIZE = 10;
    private final StepBuilderFactory stepBuilderFactory;
    private final BirthdayAccountReader accountReader;
    private final BirthdayAccountWriter accountWriter;
    private final BirthdayCouponIssueProcessor birthdayCouponIssueProcessor;
    private final LoggingListener loggingListener;
    private final BirthdayCouponListener birthdayCouponListener;
    private final BirthdayCouponSkipListener birthdayCouponSkipListener;


    /**
     * 생일 쿠폰 발급 Step.
     *
     * @return the step
     */
    @Bean
    @JobScope
    public Step issueBirthdayCouponStep() {
        log.info("============BirthdayCoupon Issue Step Start===============");
        return stepBuilderFactory.get("생일쿠폰 발급")
            .allowStartIfComplete(true)
            .<SelectAccountDto, InsertIssueCouponDto>chunk(CHUNK_SIZE)
            .reader(accountReader.birthdayAccountRead(null))
            .processor(birthdayCouponIssueProcessor)
            .writer(accountWriter.insertIssueBirthDayCoupon())
            .faultTolerant()
            .retryLimit(3)
            .retry(ConnectTimeoutException.class)
            .retry(DeadlockLoserDataAccessException.class)
            .listener(loggingListener)
            .listener(birthdayCouponListener)
            .listener(birthdayCouponSkipListener)
            .build();
    }


}
