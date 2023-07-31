package store.cookshoong.www.cookshoongspringbatch.status.reader;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
public class AccountStatusReader {
    private static final Integer PAGE_SIZE = 10;
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * Accounts reader my batis paging item reader.
     *
     * @return the my batis paging item reader
     */
    @Bean
    @StepScope
    public MyBatisPagingItemReader<AccountStatusDto> accountsReader() {
        LocalDateTime conversionBasedDate = LocalDateTime.now().minusDays(90);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("conversionBasedDate", conversionBasedDate);
        log.warn("===========reader Start!=========");
        return new MyBatisPagingItemReaderBuilder<AccountStatusDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .queryId("store.cookshoong.www.cookshoongspringbatch.status.mapper.StatusMapper.getAccounts")
            .parameterValues(parameters)
            .pageSize(PAGE_SIZE)
            .build();
    }
}
