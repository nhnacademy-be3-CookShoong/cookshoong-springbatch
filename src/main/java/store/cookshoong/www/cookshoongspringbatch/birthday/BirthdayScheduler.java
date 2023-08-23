package store.cookshoong.www.cookshoongspringbatch.birthday;

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
import store.cookshoong.www.cookshoongspringbatch.birthday.job.BirthdayCouponJobConfig;

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

    /**
     * 매달 말 오후 11시 55분에 생일쿠폰 발급이 시작됨.
     *
     * @throws JobInstanceAlreadyCompleteException the job instance already complete exception
     * @throws JobExecutionAlreadyRunningException the job execution already running exception
     * @throws JobParametersInvalidException       the job parameters invalid exception
     * @throws JobRestartException                 the job restart exception
     */
    @Scheduled(cron = "0 55 23 L * ?", zone = "Asia/Seoul")
    public void runBirthdayCoupon() {
        try {
            JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
            log.info("birthday scheduler Run! jobParameter : {}", jobParameters);
            jobLauncher.run(birthdayCouponJobConfig.issueBirthdayCouponJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | JobRestartException e) {
            log.error("birthday scheduler : {}", e.getMessage());
        }

    }
}
