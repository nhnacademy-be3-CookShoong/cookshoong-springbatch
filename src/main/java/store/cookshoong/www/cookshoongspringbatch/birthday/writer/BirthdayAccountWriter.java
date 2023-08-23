package store.cookshoong.www.cookshoongspringbatch.birthday.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.InsertIssueCouponDto;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.08.15
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BirthdayAccountWriter {
    private final SqlSessionFactory sqlSessionFactory;
    public MyBatisBatchItemWriter<InsertIssueCouponDto> insertIssueBirthDayCoupon(){
        log.warn("=========Insert into Issue Coupon DB =========");
        return new MyBatisBatchItemWriterBuilder<InsertIssueCouponDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .statementId("store.cookshoong.www.cookshoongspringbatch.birthday.mapper.BirthdayMapper.insertBirthdayCoupon")
            .build();
    }
}