package store.cookshoong.www.cookshoongspringbatch.birthday.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

/**
 * 쿠폰 정책과 기간 정보 담는 dto.
 *
 * @author seungyeon
 * @since 2023.08.13
 */
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class BirthdayCouponInfoDto {
    private Long couponPolicyId;
    private Integer usagePeriod;
}
