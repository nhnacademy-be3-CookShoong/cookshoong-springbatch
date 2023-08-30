package store.cookshoong.www.cookshoongspringbatch.birthday;

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
import store.cookshoong.www.cookshoongspringbatch.birthday.job.BirthdayCouponJobConfig;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;

/**
 * 생일쿠폰 발급 스케줄러.
 *
 * @author seungyeon
 * @since 2023.08.13
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BirthdayScheduler {
    private final JobLauncher jobLauncher;
    private final BirthdayCouponJobConfig birthdayCouponJobConfig;
    private final RestTemplate restTemplate;
    private final CommonProperties commonProperties;

    /**
     * 매월 1일 0시 0분에 생일쿠폰 발급이 시작됨.
     *
     * @throws JobInstanceAlreadyCompleteException the job instance already complete exception
     * @throws JobExecutionAlreadyRunningException the job execution already running exception
     * @throws JobParametersInvalidException       the job parameters invalid exception
     * @throws JobRestartException                 the job restart exception
     */
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void runBirthdayCoupon() {
        try {
            log.info("birthdayScheduler Start Time : {}", LocalDateTime.now());
            LocalDate today = LocalDate.now();
            int nowMonth = today.getMonthValue();
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", today.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .addString("nowMonth", String.valueOf(nowMonth))
                .toJobParameters();
            log.info("birthday scheduler Run! jobParameter : {}", jobParameters);
            jobLauncher.run(birthdayCouponJobConfig.issueBirthdayCouponJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | JobRestartException e) {
            log.error("Scheduler Error : birthday change not started... Error message : {}", e.getMessage());
            sendDoorayHook("Scheduler Error : birthday change not started... Error message : " + e.getMessage());
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
