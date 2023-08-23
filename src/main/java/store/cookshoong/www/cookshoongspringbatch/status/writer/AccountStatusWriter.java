package store.cookshoong.www.cookshoongspringbatch.status.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.status.dto.AccountStatusDto;

/**
 * 회원 상태를 휴면으로 변경하여 DB에 저장하는 Writer.
 *
 * @author seungyeon
 * @since 2023.07.31
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountStatusWriter {
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 회원 상태를 변경하여 DB에 저장.
     *
     * @return mybatis batch item writer
     */
    @Bean
    @StepScope
    public MyBatisBatchItemWriter<AccountStatusDto> changeAccounts() {
        log.info("===========Change Accounts Status Start!=========");
        return new MyBatisBatchItemWriterBuilder<AccountStatusDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .statementId("store.cookshoong.www.cookshoongspringbatch.status.mapper.StatusMapper.updateAccounts")
            .build();
    }
}
