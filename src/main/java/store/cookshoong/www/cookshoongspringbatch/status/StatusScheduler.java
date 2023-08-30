package store.cookshoong.www.cookshoongspringbatch.status;

import com.nhn.dooray.client.DoorayHook;
import com.nhn.dooray.client.DoorayHookSender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;
import store.cookshoong.www.cookshoongspringbatch.status.job.StatusJobConfig;

/**
 * 상태 변경 스케줄러.
 *
 * @author seungyeon
 * @since 2023.08.04
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StatusScheduler {
    private final JobLauncher jobLauncher;
    private final RestTemplate restTemplate;

    private final StatusJobConfig statusJobConfig;
    private final CommonProperties commonProperties;

    /**
     * 매일 새벽 5시에 상태 변경 job 실행.
     */
    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    public void runStatusChange() {
        try {
            log.info("statusScheduler Start Time : {}", LocalDateTime.now());
            LocalDate ninetyDaysAgoDate = LocalDate.now().minusDays(90);
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("ninetyDaysAgoLogin", ninetyDaysAgoDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .toJobParameters();
            log.info("status scheduler Run! jobParameter : {}", jobParameters);
            jobLauncher.run(statusJobConfig.changedStatusJob(), jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException
                 | JobParametersInvalidException | JobRestartException e) {
            log.error("Scheduler Error : status change not started... Error message : {}", e.getMessage());
            sendDoorayHook("Scheduler Error : status change not started... Error message : "+ e.getMessage());
        }
    }

    private void sendDoorayHook(String text) {
        try {
            new DoorayHookSender(restTemplate, commonProperties.getHookUrl())
                .send(DoorayHook.builder()
                    .botName("쿡슝 스케줄러 알림봇")
                    .text(text)
                    .build());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("[FAILED] " + LocalDate.now()
                + ", 회원 상태 변경 스케줄러 MessageSender 오류가 발생하였습니다. ErrorMessage : {}", e.getMessage());
        }
    }
}
