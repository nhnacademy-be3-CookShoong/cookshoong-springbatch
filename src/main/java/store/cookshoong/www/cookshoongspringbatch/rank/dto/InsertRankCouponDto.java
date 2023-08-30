package store.cookshoong.www.cookshoongspringbatch.rank.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Getter
@Setter
public class InsertRankCouponDto {
    private Long accountId;
    private Long couponPolicyId;
    private LocalDate startDate;
    private LocalDate endDate;
}
