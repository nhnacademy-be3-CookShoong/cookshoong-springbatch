package store.cookshoong.www.cookshoongspringbatch.birthday.mapper;

import java.util.List;
import java.util.stream.LongStream;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.BirthdayCouponInfoDto;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.InsertIssueCouponDto;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.SelectAccountDto;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.08.15
 */
public interface BirthdayMapper {
    BirthdayCouponInfoDto selectBirthdayCouponInfo();
    List<SelectAccountDto> selectAccountsByMonth();
    void insertBirthdayCoupon(InsertIssueCouponDto dto);
}
