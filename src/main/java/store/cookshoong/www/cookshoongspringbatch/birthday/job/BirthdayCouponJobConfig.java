package store.cookshoong.www.cookshoongspringbatch.birthday.job;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 매달 생일인 회원을 찾고, 해당 회원에게 쿠폰을 발급하는 Job.
 *
 * @author seungyeon
 * @since 2023.08.05
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BirthdayCouponJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final BirthdayCouponInfoStepConfig birthdayCouponInfoStepConfig;
    private final BirthdayCouponStepConfig birthdayCouponStepConfig;

    /**
     * 생일 쿠폰에 대한 정책을 들고와서 쿠폰 발급.
     *
     * @return the job
     */
    @Bean
    public Job issueBirthdayCouponJob() {
        return jobBuilderFactory.get("issueBirthdayCoupon_" + LocalDateTime.now())
            .start(birthdayCouponInfoStepConfig.getCouponInfoStep())
            .next(birthdayCouponStepConfig.issueBirthdayCouponStep())
            .build();
    }
}
