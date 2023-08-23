package store.cookshoong.www.cookshoongspringbatch.rank;

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

    /**
     * 매달 4일 오후 11시 55분에 등급 변경 후 쿠폰 발급하는 job 실행.
     *
     * @throws JobInstanceAlreadyCompleteException the job instance already complete exception
     * @throws JobExecutionAlreadyRunningException the job execution already running exception
     * @throws JobParametersInvalidException       the job parameters invalid exception
     * @throws JobRestartException                 the job restart exception
     */
    @Scheduled(cron = "0 55 23 4 * ?", zone = "Asia/Seoul")
    public void runAccountRank()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        log.info("rank scheduler Run! jobParameter : {}", jobParameters);
        jobLauncher.run(accountRankJobConfig.changedRankJob(), jobParameters);
    }
}
