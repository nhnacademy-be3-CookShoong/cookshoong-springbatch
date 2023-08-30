package store.cookshoong.www.cookshoongspringbatch.rank.writer;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.InsertRankCouponDto;
import store.cookshoong.www.cookshoongspringbatch.rank.mapper.RankMapper;

/**
 * 등급쿠폰 발급 writer.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RankCouponWriter {
    private final SqlSessionFactory sqlSessionFactory;
    private static final int CHUNK_SIZE = 10000;

    /**
     * Insert rank coupon writer item writer.
     *
     * @return the item writer
     */
    @StepScope
    @Bean
    public ItemWriter<InsertRankCouponDto> insertRankCouponWriter() {

        return items -> {
            log.info("=============== Bulk Insert Rank Coupon Start! =================");
            List<InsertRankCouponDto> insertItems = new ArrayList<>();

            for (InsertRankCouponDto item : items) {
                insertItems.add(item);

                if (insertItems.size() >= CHUNK_SIZE) { // Chunk 크기에 도달하면 Bulk Insert 수행
                    performBulkInsert(insertItems);
                    insertItems.clear();
                }
            }

            // 마지막 Chunk Bulk Insert 수행
            if (!insertItems.isEmpty()) {
                performBulkInsert(insertItems);
            }

            log.info("=============== Bulk Insert Rank Coupon Finished! =================");
        };
    }

    private void performBulkInsert(List<InsertRankCouponDto> batchItems) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            RankMapper rankMapper = sqlSession.getMapper(RankMapper.class);
            rankMapper.disableConstraints();
            long start = System.currentTimeMillis();
            log.error("== 10000 Bulk insert Start == : {}", start);
            rankMapper.insertRankCoupon(batchItems);
            long end = System.currentTimeMillis() - start;
            log.error("== 10000 Bulk insert Total == : {} ms", end);
            rankMapper.enableConstraints();
            sqlSession.commit();
        }
    }
}
