package store.cookshoong.www.cookshoongspringbatch.birthday.job;

import com.nhn.dooray.client.DoorayHook;
import com.nhn.dooray.client.DoorayHookSender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;

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
    private final CommonProperties commonProperties;

    private final RestTemplate restTemplate;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * 생일 쿠폰에 대한 정책을 들고와서 쿠폰 발급.
     *
     * @return the job
     */
    @Bean
    public Job issueBirthdayCouponJob() {
        return jobBuilderFactory.get("issueBirthdayCoupon_" + LocalDateTime.now())
            .start(birthdayCouponInfoStepConfig.getCouponInfoStep())
            .on("COMPLETED")
            .to(birthdayCouponStepConfig.issueBirthdayCouponStep())
            .from(birthdayCouponStepConfig.issueBirthdayCouponStep())
            .on("FAILED")
            .to(sendFailIssueCouponStep())
            .from(birthdayCouponStepConfig.issueBirthdayCouponStep())
            .on("COMPLETED")
            .to(sucessBirthdayCouponStep())
            .end()
            .build();
    }

    /**
     * 해당 Step 실패시 job 실패와 같음 -> 실패시 관리자가 알 수 있도록 바로 알림메시지 보냄.
     *
     * @return the step
     */
    @Bean
    public Step sendFailIssueCouponStep() {
        return stepBuilderFactory.get("생일쿠폰 발급 실패")
            .tasklet((stepContribution, chunkContext) -> {
                String message = "[FAILED] " + LocalDate.now() + ", 생일 쿠폰 발급 스텝에서 문제가 발생하였습니다. 확인 부탁드립니다.";
                sendDoorayHook(message);
                stepContribution.setExitStatus(ExitStatus.FAILED);
                chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED);
                return RepeatStatus.FINISHED;
            }).build();
    }
    private void sendDoorayHook(String text) {
        try {
            new DoorayHookSender(restTemplate, commonProperties.getHookUrl())
                .send(DoorayHook.builder()
                    .botName("생일 쿠폰 알림봇")
                    .text(text)
                    .build());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("[FAILED] " + LocalDate.now()
                + ", 생일 쿠폰 MessageSender에서 오류가 발생하였습니다. ErrorMessage : {}", e.getMessage());
        }
    }


    /**
     * Step이 성공시 : 성공 완료 알림 log.
     *
     * @return the step
     */
    @Bean
    Step sucessBirthdayCouponStep() {
        return stepBuilderFactory.get("생일 쿠폰 발급 완료")
            .tasklet((stepContribution, chunkContext) -> {
                String message = "[SUCCESS] " + LocalDate.now() + ", birthday status : COMPLETED";
                log.info(message);
                return RepeatStatus.FINISHED;
            }).build();
    }
}
