package store.cookshoong.www.cookshoongspringbatch.rank.reader;

import java.time.LocalDate;
import java.time.YearMonth;
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
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountOrderDto;

/**
 * 회원아이디와 주문횟수를 가져오는 Reader.
 *
 * @author seungyeon
 * @since 2023.08.01
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountRankReader {
    private static final Integer PAGE_SIZE = 10;
    private final SqlSessionFactory sqlSessionFactory;


    /**
     * 전 달을 기준, '배달완료'상태의 주문 건수를 가져옴.
     *
     * @return  my batis paging item reader
     */
    @Bean
    @StepScope
    public MyBatisPagingItemReader<SelectAccountOrderDto> accountRankRead() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate startDate = lastMonth.atDay(1);
        LocalDate endDate = lastMonth.atEndOfMonth();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("statusCode1", "COMPLETE");
        parameters.put("statusCode2", "PARTIAL");
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);

        log.warn("============Rank Step Reader Start===============");
        return new MyBatisPagingItemReaderBuilder<SelectAccountOrderDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .queryId("store.cookshoong.www.cookshoongspringbatch.rank.mapper.RankMapper.selectOrderCntByAccount")
            .parameterValues(parameters)
            .pageSize(PAGE_SIZE)
            .build();
    }
}
