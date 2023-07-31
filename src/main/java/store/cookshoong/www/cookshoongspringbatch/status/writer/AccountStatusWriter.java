package store.cookshoong.www.cookshoongspringbatch.status.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.status.AccountStatusDto;

/**
 * {설명을 작성해주세요}.
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
     * Change accounts my batis batch item writer.
     *
     * @return mybatis batch item writer
     */
    @Bean
    public MyBatisBatchItemWriter<AccountStatusDto> changeAccounts() {
        log.warn("===========writer Start!=========");
        return new MyBatisBatchItemWriterBuilder<AccountStatusDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .statementId("store.cookshoong.www.cookshoongspringbatch.status.mapper.StatusMapper.updateAccounts")
            .build();
    }
}
