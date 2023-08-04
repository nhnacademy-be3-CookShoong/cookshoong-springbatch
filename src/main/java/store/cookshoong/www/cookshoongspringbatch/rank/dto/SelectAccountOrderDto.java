package store.cookshoong.www.cookshoongspringbatch.rank.dto;

import lombok.Getter;

/**
 * 회원의 주문 횟수를 가져오는 dto.
 *
 * @author seungyeon (유승연)
 * @since 2023.08.01
 */
@Getter
public class SelectAccountOrderDto {
    private Long accountId;
    private Integer orderCnt;
}
