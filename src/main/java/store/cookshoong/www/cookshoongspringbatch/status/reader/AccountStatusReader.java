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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.status.dto.AccountStatusDto;

/**
 * 사용자 상태 변경을 위한 Reader.
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
     * 로그인한 마지막 날짜가 90일 이전, 현재 상태가 ACTIVE인 회원을 페이징 단위로 읽어옴.
     *
     * @return the my batis paging item reader
     */
    @Bean
    @StepScope
    public MyBatisPagingItemReader<AccountStatusDto> accountsReader(
        @Value("#{jobParameters['ninetyDaysAgoLogin']}") String ninetyDaysAgoLogin
    ) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("conversionBasedDate", ninetyDaysAgoLogin);
        log.info("===========Status Step reader Start!=========");
        return new MyBatisPagingItemReaderBuilder<AccountStatusDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .queryId("store.cookshoong.www.cookshoongspringbatch.status.mapper.StatusMapper.getAccounts")
            .parameterValues(parameters)
            .pageSize(PAGE_SIZE)
            .build();
    }
}
