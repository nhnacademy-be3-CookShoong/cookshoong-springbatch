package store.cookshoong.www.cookshoongspringbatch.status.job;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 회원 상태 변경을 위한 job.
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
     * 회원 상태 변경을 위한 job.
     *
     * @return the job
     */
    @Bean
    public Job changedStatusJob() {
        return jobBuilderFactory.get("changeStatusJob_" + UUID.randomUUID())
            .start(statusStepConfig.changeStatusJobStep())
            .build();
    }
}
