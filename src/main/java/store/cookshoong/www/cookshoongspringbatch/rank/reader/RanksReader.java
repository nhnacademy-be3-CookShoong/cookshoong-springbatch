package store.cookshoong.www.cookshoongspringbatch.rank.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;

/**
 * 등급에 대한 리스트를 읽어오는 reader.
 *
 * @author seungyeon
 * @since 2023.08.03
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RanksReader {
    private static final Integer PAGE_SIZE = 10;
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 회원등급 : LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4 읽어오기.
     *
     * @return the rank codes
     */
    @Bean
    public MyBatisPagingItemReader<RankDto> getRankCodes() {
        log.warn("=========get Rank List==============");
        return new MyBatisPagingItemReaderBuilder<RankDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .queryId("store.cookshoong.www.cookshoongspringbatch.rank.mapper.RankMapper.selectRankCodes")
            .pageSize(PAGE_SIZE)
            .build();
    }
}
