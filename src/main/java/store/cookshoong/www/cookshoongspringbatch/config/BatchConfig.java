package store.cookshoong.www.cookshoongspringbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.BirthdayCouponInfoDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;

/**
 * Batch 설정.
 *
 * @author seungyeon
 * @since 2023.08.23
 */
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobRegistry jobRegistry;

    /**
     * Job registry bean processor bean post processor.
     *
     * @return the bean post processor
     */
    @Bean
    public BeanPostProcessor jobRegistryBeanProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    /**
     * jobExecutionContext 역직렬화 문제 해결.
     *
     * @return the execution context serializer
     */
    @Bean
    public ExecutionContextSerializer customSerializer() {
        return new Jackson2ExecutionContextStringSerializer(
            BirthdayCouponInfoDto.class.getName(),
            RankDto.class.getName());
    }
}
