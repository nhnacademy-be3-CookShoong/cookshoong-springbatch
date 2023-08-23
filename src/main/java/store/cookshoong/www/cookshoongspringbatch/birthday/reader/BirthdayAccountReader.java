package store.cookshoong.www.cookshoongspringbatch.birthday.reader;

import java.time.LocalDate;
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
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.SelectAccountDto;

/**
 * 이번달 생일인 회원 번호 reader.
 *
 * @author seungyeon
 * @since 2023.08.15
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BirthdayAccountReader {
    private static final Integer PAGE_SIZE = 10;
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 이번 달 생일인 회원 번호 read.
     *
     * @return my batis paging item reader
     */
    @Bean
    @StepScope
    public MyBatisPagingItemReader<SelectAccountDto> birthdayAccountRead() {
        int nowMonth = LocalDate.now().getMonthValue();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nowMonth", nowMonth);

        log.warn("===========BirthdayCoupon Issue Step Reader Start ===========");
        return new MyBatisPagingItemReaderBuilder<SelectAccountDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .queryId("store.cookshoong.www.cookshoongspringbatch.birthday.mapper.BirthdayMapper.selectAccountsByMonth")
            .parameterValues(parameters)
            .pageSize(PAGE_SIZE)
            .build();
    }
}
