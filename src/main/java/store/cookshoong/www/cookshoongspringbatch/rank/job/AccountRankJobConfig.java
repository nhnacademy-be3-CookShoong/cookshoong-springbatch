package store.cookshoong.www.cookshoongspringbatch.rank.job;

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
 * 매달 주문 횟수에 의해 회원 등급을 산정하고, 쿠폰을 발급하는 Job.
 *
 * @author seungyeon (유승연)
 * @since 2023.08.01
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountRankJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final AccountRankStepConfig accountRankStepConfig;
    private final RankCouponIssueStepConfig rankCouponIssueStepConfig;
    private final RankStepConfig rankStepConfig;
    private final StepBuilderFactory stepBuilderFactory;
    private final CommonProperties commonProperties;
    private final RestTemplate restTemplate;

    /**
     * 등급 변경 Job : 등급 데이터베이스에서 가져옴 - 회원 주문 횟수에 따라 등급 재산정 - 재산정된 등급에 의해 쿠폰 발급.
     * (쿠폰 발급에 대한 Step은 아직 작성하지 않음.)
     * @return the job
     */
    @Bean
    public Job changedRankJob() {
        return jobBuilderFactory.get("changeRankJob_" + LocalDateTime.now())
            .start(rankStepConfig.getRankInfoStep()) // rank list 들고옴
                .on("COMPLETED")
                .to(accountRankStepConfig.changeRankStep())
            .from(rankStepConfig.getRankInfoStep())
                .on("*")
                .to(sendFailRankInfoStep())
            .from(accountRankStepConfig.changeRankStep())
                .on("COMPLETED")
                .to(rankCouponIssueStepConfig.issueRankCouponStep())
            .from(accountRankStepConfig.changeRankStep())
                .on("*")
                .to(sendFailUpdateAccountStep())
            .from(rankCouponIssueStepConfig.issueRankCouponStep())
                .on("COMPLETED")
                .to(sucessRankCouponStep())
            .from(rankCouponIssueStepConfig.issueRankCouponStep())
                .on("*")
                .to(sendFailIssueRankCouponStep())
            .end()
            .build();
    }

    /**
     * 해당 Step 실패시 job 실패와 같음 -> 실패시 관리자가 알 수 있도록 바로 알림메시지 보냄.
     *
     * @return the step
     */
    @Bean
    public Step sendFailRankInfoStep() {
        return stepBuilderFactory.get("rankInfoFailStep")
            .tasklet((stepContribution, chunkContext) -> {
                String message = "[FAILED] " + LocalDate.now() + ", 등급 정보 스텝에서 문제가 발생하였습니다. 확인 부탁드립니다.";
                sendDoorayHook(message);
                stepContribution.setExitStatus(ExitStatus.FAILED);
                chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED);
                return RepeatStatus.FINISHED;
            }).build();
    }

    /**
     * 해당 Step 실패시 job 실패와 같음 -> 실패시 관리자가 알 수 있도록 바로 알림메시지 보냄.
     *
     * @return the step
     */
    @Bean
    public Step sendFailUpdateAccountStep() {
        return stepBuilderFactory.get("sendFailUpdateAccountStep")
            .tasklet((stepContribution, chunkContext) -> {
                String message = "[FAILED] " + LocalDate.now() + ", 등급 갱신 스텝에서 문제가 발생하였습니다. 확인 부탁드립니다.";
                sendDoorayHook(message);
                stepContribution.setExitStatus(ExitStatus.FAILED);
                chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED);
                return RepeatStatus.FINISHED;
            }).build();
    }

    /**
     * 해당 Step 실패시 job 실패와 같음 -> 실패시 관리자가 알 수 있도록 바로 알림메시지 보냄.
     *
     * @return the step
     */
    @Bean
    public Step sendFailIssueRankCouponStep() {
        return stepBuilderFactory.get("sendFailIssueRankCouponStep")
            .tasklet((stepContribution, chunkContext) -> {
                String message = "[FAILED] " + LocalDate.now() + ", 등급쿠폰 발급 스텝에서 문제가 발생하였습니다. 확인 부탁드립니다.";
                sendDoorayHook(message);
                stepContribution.setExitStatus(ExitStatus.FAILED);
                chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED);
                return RepeatStatus.FINISHED;
            }).build();
    }
    /**
     * Step이 성공시 : 성공 완료 알림 log.
     *
     * @return the step
     */
    @Bean
    Step sucessRankCouponStep() {
        return stepBuilderFactory.get("rankCouponIssueStep")
            .tasklet((stepContribution, chunkContext) -> {
                String message = "[SUCCESS] " + LocalDate.now() + ", rankCoupon Issued, status : COMPLETED";
                log.info(message);
                return RepeatStatus.FINISHED;
            }).build();
    }
    private void sendDoorayHook(String text) {
        try {
            new DoorayHookSender(restTemplate, commonProperties.getHookUrl())
                .send(DoorayHook.builder()
                    .botName("등급 쿠폰 알림봇")
                    .text(text)
                    .build());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("[FAILED] " + LocalDate.now()
                + ", 등급 쿠폰 MessageSender에서 오류가 발생하였습니다. ErrorMessage : {}", e.getMessage());
        }
    }

}
