package store.cookshoong.www.cookshoongspringbatch.rank.dto;

import lombok.Getter;

/**
 * 회원 등급 쿠폰 발급을 위해 등급 정보 가져오는 dto.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Getter
public class SelectAccountRankCouponDto {
    private Long accountId;
    private String rankCode;
}
