package store.cookshoong.www.cookshoongspringbatch.rank.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountRankCouponDto;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RankCouponReader {
    private static final Integer PAGE_SIZE = 10;
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * Rank coupon read my batis paging item reader.
     *
     * @return the my batis paging item reader
     */
    @Bean
    @StepScope
    public MyBatisPagingItemReader<SelectAccountRankCouponDto> rankCouponRead() {
        log.error("============RankCoupon Step Reader Start===============");
        return new MyBatisPagingItemReaderBuilder<SelectAccountRankCouponDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .queryId("store.cookshoong.www.cookshoongspringbatch.rank.mapper.RankMapper.selectAccountRankCoupon")
            .pageSize(PAGE_SIZE)
            .build();
    }
}
