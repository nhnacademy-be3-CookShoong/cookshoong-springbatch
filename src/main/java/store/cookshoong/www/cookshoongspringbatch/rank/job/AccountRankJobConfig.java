package store.cookshoong.www.cookshoongspringbatch.rank.job;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 매달 주문 횟수에 의해 회원 등급을 산정하고, 쿠폰을 발급하는 Job.
 *
 * @author seungyeon (유승연)
 * @since 2023.08.01
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountRankJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final AccountRankStepConfig accountRankStepConfig;
    private final RankStepConfig rankStepConfig;

    /**
     * 등급 변경 Job : 등급 데이터베이스에서 가져옴 - 회원 주문 횟수에 따라 등급 재산정 - 재산정된 등급에 의해 쿠폰 발급.
     * (쿠폰 발급에 대한 Step은 아직 작성하지 않음.)
     * @return the job
     */
    @Bean
    public Job changedRankJob() {
        return jobBuilderFactory.get("changeRankJob_" + LocalDateTime.now())
            .start(rankStepConfig.getRankInfoStep()) // rank list 들고옴
            .next(accountRankStepConfig.changeRankStep())
            .build();
    }

}
