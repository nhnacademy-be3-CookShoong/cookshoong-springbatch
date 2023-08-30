package store.cookshoong.www.cookshoongspringbatch.rank.dto;

import java.time.LocalDate;
import lombok.Getter;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Getter
public class SelectAccountRankCouponDto {
    private Long accountId;
    private Long couponPolicyId;
    private Integer usagePeriod;
}
