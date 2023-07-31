package store.cookshoong.www.cookshoongspringbatch.status.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;
import store.cookshoong.www.cookshoongspringbatch.status.AccountStatusDto;
import store.cookshoong.www.cookshoongspringbatch.status.exception.NotFoundAccountException;
import store.cookshoong.www.cookshoongspringbatch.status.reader.AccountStatusReader;
import store.cookshoong.www.cookshoongspringbatch.status.writer.AccountStatusWriter;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.07.31
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StatusStepConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final AccountStatusReader accountStatusReader;
    private final AccountStatusWriter accountStatusWriter;

    private static final Integer CHUNK_SIZE = 10;

    @JobScope
    @Bean
    public Step changeStatusJobStep() {
        return stepBuilderFactory.get("휴면될 회원리스트 가져오기")
            .<AccountStatusDto, AccountStatusDto>chunk(CHUNK_SIZE)
            .reader(accountStatusReader.accountsReader())
            .writer(accountStatusWriter.changeAccounts())
            .build();
    }
}
