package store.cookshoong.www.cookshoongspringbatch.status;

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

    private final StatusJobConfig statusJobConfig;

    /**
     * 매일 새벽 4시에 상태 변경 job 실행.
     */
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    public void runStatusChange() {
        try {
            JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
            log.warn("status scheduler Run! jobParameter : {}", jobParameters);
            jobLauncher.run(statusJobConfig.changedStatusJob(), jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }
}
