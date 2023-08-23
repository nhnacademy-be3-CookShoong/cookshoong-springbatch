package store.cookshoong.www.cookshoongspringbatch.rank.writer;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
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
public class CompositeRankWriter {
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * Composite rank and coupon writer composite item writer.
     *
     * @return the composite item writer
     */
    @Bean
    @StepScope
    public CompositeItemWriter<UpdateAccountRankDto> compositeRankAndCouponWriter() {
        List<ItemWriter<? super UpdateAccountRankDto>> writers = new ArrayList<>(2);
        writers.add(updateAccountRank());
        writers.add(insertRankCoupon());

        CompositeItemWriter<UpdateAccountRankDto> itemWriter = new CompositeItemWriter<>();
        itemWriter.setDelegates(writers);
        return itemWriter;
    }

    /**
     * 갱신된 회원 정보 DB에 저장.
     *
     * @return the my batis batch item writer
     */
    @StepScope
    @Bean
    public MyBatisBatchItemWriter<UpdateAccountRankDto> updateAccountRank() {
        log.debug("===============update Account Rank Start!=================");
        return new MyBatisBatchItemWriterBuilder<UpdateAccountRankDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .statementId("store.cookshoong.www.cookshoongspringbatch.rank.mapper.RankMapper.updateRank")
            .build();
    }

    /**
     * Insert rank coupon my batis batch item writer.
     *
     * @return the my batis batch item writer
     */
    @StepScope
    @Bean
    public MyBatisBatchItemWriter<UpdateAccountRankDto> insertRankCoupon() {
        log.debug("=============insert Account Rank Coupon!===================");
        return new MyBatisBatchItemWriterBuilder<UpdateAccountRankDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .statementId("store.cookshoong.www.cookshoongspringbatch.rank.mapper.RankMapper.insertRankCoupon")
            .build();
    }
}
