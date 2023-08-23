package store.cookshoong.www.cookshoongspringbatch.birthday.job;

import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.BirthdayCouponInfoDto;
import store.cookshoong.www.cookshoongspringbatch.birthday.reader.CouponInfoReader;
import store.cookshoong.www.cookshoongspringbatch.birthday.writer.CouponInfoWriter;
import store.cookshoong.www.cookshoongspringbatch.logging.LoggingListener;

/**
 * 생일 쿠폰 정책에 대한 정보를 가져오는 Step.
 *
 * @author seungyeon
 * @since 2023.08.13
 */
@Configuration
@RequiredArgsConstructor
public class BirthdayCouponInfoStepConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final LoggingListener loggingListener;
    private final CouponInfoReader couponInfoReader;
    private final CouponInfoWriter couponInfoWriter;

    /**
     * 생일 쿠폰에 대한 정책은 static 변수로 지정하고 1개만 가져와서 사용하고 있다.
     *
     * @return the coupon info step
     */
    @Bean
    @JobScope
    public Step getCouponInfoStep() {
        return stepBuilderFactory.get("생일 쿠폰 정보 가져오기")
            .allowStartIfComplete(true)
            .<BirthdayCouponInfoDto, BirthdayCouponInfoDto>chunk(1)
            .reader(couponInfoReader.getBirthCouponInfo())
            .writer(couponInfoWriter)
            .faultTolerant()
            .retry(ConnectTimeoutException.class)
            .retry(DeadlockLoserDataAccessException.class)
            .listener(promotionListener_birthday())
            .listener(loggingListener)
            .build();
    }

    /**
     * jobExecutionContext로 승격.
     *
     * @return the execution context promotion listener
     */
    @JobScope
    @Bean
    public ExecutionContextPromotionListener promotionListener_birthday() {
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        executionContextPromotionListener.setKeys(new String[]{"birthdayCoupon"});
        return executionContextPromotionListener;
    }
}
