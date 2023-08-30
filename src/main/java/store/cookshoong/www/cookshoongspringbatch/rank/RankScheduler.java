package store.cookshoong.www.cookshoongspringbatch.rank;

import com.nhn.dooray.client.DoorayHook;
import com.nhn.dooray.client.DoorayHookSender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
import store.cookshoong.www.cookshoongspringbatch.rank.job.AccountRankJobConfig;

/**
 * 등급 변경 및 등급 쿠폰 발급 스케줄러.
 *
 * @author seungyeon
 * @since 2023.08.23
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RankScheduler {
    private final JobLauncher jobLauncher;
    private final AccountRankJobConfig accountRankJobConfig;
    private final RestTemplate restTemplate;
    private final CommonProperties commonProperties;

    /**
     * 매달 4일 오후 11시 55분에 등급 변경 후 쿠폰 발급하는 job 실행.
     *
     * @throws JobInstanceAlreadyCompleteException the job instance already complete exception
     * @throws JobExecutionAlreadyRunningException the job execution already running exception
     * @throws JobParametersInvalidException       the job parameters invalid exception
     * @throws JobRestartException                 the job restart exception
     */
    @Scheduled(cron = "0 55 23 4 * ?", zone = "Asia/Seoul")
    public void runAccountRank() {

        try {
            log.info("rank scheduler  Start Time jobParameter : {}", LocalDateTime.now());
            YearMonth lastMonth = YearMonth.now().minusMonths(1);
            LocalDate startDate = lastMonth.atDay(1);
            LocalDate endDate = lastMonth.atEndOfMonth();
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("rankUpdateStartDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .addString("rankUpdateEndDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .addString("today", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .toJobParameters();
            log.info("account-rank scheduler Run! jobParameter : {}", jobParameters);
            jobLauncher.run(accountRankJobConfig.changedRankJob(), jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException
                 | JobParametersInvalidException | JobRestartException e) {
            log.error("Scheduler Error : account-rank not started... Error message : {}", e.getMessage());
            sendDoorayHook("Scheduler Error : account-rank not started... Error message : " + e.getMessage());
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
                + ", 회원 등급 변경 및 쿠폰 발급 스케줄러 MessageSender 오류가 발생하였습니다. ErrorMessage : {}", e.getMessage());
        }
    }
}
