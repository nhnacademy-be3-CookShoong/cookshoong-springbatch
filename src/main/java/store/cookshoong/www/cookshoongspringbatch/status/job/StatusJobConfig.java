package store.cookshoong.www.cookshoongspringbatch.status.job;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.07.30
 */
@Configuration
@RequiredArgsConstructor
public class StatusJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StatusStepConfig statusStepConfig;

    /**
     * Change status job job.
     *
     * @return the job
     */
    @Bean
    public Job changeStatusJob() {
        return jobBuilderFactory.get("changeStatus_" + LocalDateTime.now())
            .preventRestart()
            .start(statusStepConfig.changeStatusJobStep())
            .build();
    }


}
