package store.cookshoong.www.cookshoongspringbatch.status.job;

import com.nhn.dooray.client.DoorayHook;
import com.nhn.dooray.client.DoorayHookSender;
import java.time.LocalDate;
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
 * 회원 상태 변경을 위한 job.
 *
 * @author seungyeon
 * @since 2023.07.30
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class StatusJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StatusStepConfig statusStepConfig;
    private final RestTemplate restTemplate;
    private final StepBuilderFactory stepBuilderFactory;

    private final CommonProperties commonProperties;

    /**
     * 회원 상태 변경을 위한 job.
     *
     * @return the job
     */
    @Bean
    public Job changedStatusJob() {
        return jobBuilderFactory.get("changeStatusJob_" + LocalDate.now())
            .start(statusStepConfig.changeStatusJobStep())
            .on("COMPLETED")
            .to(sucessStep())
            .from(statusStepConfig.changeStatusJobStep())
            .on("*")
            .to(sendFailStep())
            .end()
            .build();
    }

    /**
     * Step이 성공시 : 성공 완료 알림 log.
     *
     * @return the step
     */
    @Bean
    Step sucessStep() {
        return stepBuilderFactory.get("statusChangeSuccessStep")
            .tasklet((stepContribution, chunkContext) -> {
                String message = "[SUCCESS] " + LocalDate.now() + ", statusChange status : COMPLETED";
                log.info(message);
                return RepeatStatus.FINISHED;
            }).build();
    }

    /**
     * 해당 Step 실패시 job 실패와 같음 -> 실패시 관리자가 알 수 있도록 바로 알림메시지 보냄.
     *
     * @return the step
     */
    @Bean
    public Step sendFailStep() {
        return stepBuilderFactory.get("statusChangeFailStep")
            .tasklet((stepContribution, chunkContext) -> {
                String message = "[FAILED] " + LocalDate.now() + ", 회원 전환 스텝에서 문제가 발생하였습니다. 확인 부탁드립니다.";
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
                    .botName("휴면 전환 알림봇")
                    .text(text)
                    .build());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("[FAILED] " + LocalDate.now()
                + ", 회원 상태 변경 MessageSender에서 오류가 발생하였습니다. ErrorMessage : {}", e.getMessage());
        }
    }
}
