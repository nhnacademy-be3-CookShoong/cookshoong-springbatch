package store.cookshoong.www.cookshoongspringbatch.birthday.reader;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.birthday.BirthdayProperties;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.BirthdayCouponInfoDto;

/**
 * 생일 쿠폰 정책에 대한 정보 가져오는 Reader.
 *
 * @author seungyeon
 * @since 2023.08.15
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CouponInfoReader {
    private final BirthdayProperties birthdayProperties;

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 생일 쿠폰 정책에 대해 가져오기 위한 reader : 생일쿠폰 정책 id, 사용기간을 가져옵니다.
     *
     * @return the birth coupon info
     */
    @Bean
    public MyBatisCursorItemReader<BirthdayCouponInfoDto> getBirthCouponInfo() {
        log.warn("========get Birthday Coupon Info=========");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("birthCouponCode", birthdayProperties.getBirthdayPolicy());
        return new MyBatisCursorItemReaderBuilder<BirthdayCouponInfoDto>()
            .sqlSessionFactory(sqlSessionFactory)
            .queryId("store.cookshoong.www.cookshoongspringbatch.birthday.mapper.BirthdayMapper.selectBirthdayCouponInfo")
            .parameterValues(parameters)
            .build();
    }
}
