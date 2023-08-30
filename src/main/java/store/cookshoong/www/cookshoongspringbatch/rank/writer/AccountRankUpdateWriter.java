package store.cookshoong.www.cookshoongspringbatch.rank.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.UpdateAccountRankDto;

/**
 * 회원 등급 갱신 후 DB에 저장.
 *
 * @author seungyeon
 * @since 2023.08.01
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountRankUpdateWriter {
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * Update account rank my batis batch item writer.
     *
     * @return the my batis batch item writer
     */
    @StepScope
    @Bean
    public MyBatisBatchItemWriter<UpdateAccountRankDto> updateAccountRank() {
        log.error("===============update Account Rank Start!=================");
        return new MyBatisBatchItemWriterBuilder<UpdateAccountRankDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .statementId("store.cookshoong.www.cookshoongspringbatch.rank.mapper.RankMapper.updateRank")
            .build();
    }

}
