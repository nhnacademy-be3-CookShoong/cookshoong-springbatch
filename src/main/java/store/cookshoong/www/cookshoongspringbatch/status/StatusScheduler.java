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
     * 매일 새벽 5시에 상태 변경 job 실행.
     */
    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    public void runStatusChange()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        log.info("status scheduler Run! jobParameter : {}", jobParameters);
        jobLauncher.run(statusJobConfig.changedStatusJob(), jobParameters);
    }
}
