package store.cookshoong.www.cookshoongspringbatch.birthday.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.InsertIssueCouponDto;

/**
 * 생일 쿠폰 발급 writer.
 *
 * @author seungyeon
 * @since 2023.08.15
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BirthdayAccountWriter {
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 생일쿠폰 발급 writer.
     *
     * @return the my batis batch item writer
     */
    public MyBatisBatchItemWriter<InsertIssueCouponDto> insertIssueBirthDayCoupon() {
        log.info("=========Insert into Issue Coupon DB =========");
        return new MyBatisBatchItemWriterBuilder<InsertIssueCouponDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .statementId("store.cookshoong.www.cookshoongspringbatch.birthday.mapper.BirthdayMapper.insertBirthdayCoupon")
            .build();
    }
}
