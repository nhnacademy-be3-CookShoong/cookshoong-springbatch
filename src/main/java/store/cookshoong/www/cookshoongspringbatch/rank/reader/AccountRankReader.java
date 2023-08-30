package store.cookshoong.www.cookshoongspringbatch.rank.reader;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
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
    private final SqlSessionFactory sqlSessionFactory;


    /**
     * 전 달을 기준, '배달완료'상태의 주문 건수를 가져옴.
     *
     * @return my batis paging item reader
     */
    @Bean
    @StepScope
    public MyBatisCursorItemReader<SelectAccountOrderDto> accountRankRead(
        @Value("#{jobParameters['rankUpdateStartDate']}") String startDate,
        @Value("#{jobParameters['rankUpdateEndDate']}") String endDate
    ) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("statusCode1", "COMPLETE");
        parameters.put("statusCode2", "PARTIAL");
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);

        log.info("============Rank Step Reader Start===============");
        return new MyBatisCursorItemReaderBuilder<SelectAccountOrderDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .queryId("store.cookshoong.www.cookshoongspringbatch.rank.mapper.RankMapper.selectOrderCntByAccount")
            .parameterValues(parameters)
            .build();
    }
}
