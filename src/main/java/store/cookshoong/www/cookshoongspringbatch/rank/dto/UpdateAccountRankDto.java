package store.cookshoong.www.cookshoongspringbatch.rank.dto;

import java.time.LocalDate;
import lombok.Getter;

/**
 * 회원의 등급을 재설정하기 위한 dto.
 *
 * @author seungyeon
 * @since 2023.08.01
 */
@Getter
public class UpdateAccountRankDto {
    private Long accountId;
    private String rankCode;
    private Long couponPolicyId;

    private LocalDate receiptDate;
    private LocalDate expirationDate;

    public void modify(Long accountId, String rankCode, Long couponPolicyId,
                       LocalDate receiptDate, LocalDate expirationDate) {
        this.accountId = accountId;
        this.rankCode = rankCode;
        this.couponPolicyId = couponPolicyId;
        this.receiptDate = receiptDate;
        this.expirationDate = expirationDate;
    }
}
