package store.cookshoong.www.cookshoongspringbatch.birthday.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 생일 쿠폰 발급을 위한 dto.
 *
 * @author seungyeon
 * @since 2023.08.13
 */
@Getter
@AllArgsConstructor
public class InsertIssueCouponDto {
    private Long accountId;
    private Long couponPolicyId;
    private LocalDate receiptDate;
    private LocalDate expirationDate;
}
