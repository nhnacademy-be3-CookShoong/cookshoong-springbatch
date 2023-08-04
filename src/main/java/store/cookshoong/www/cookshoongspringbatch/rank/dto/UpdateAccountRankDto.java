package store.cookshoong.www.cookshoongspringbatch.rank.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원의 등급을 재설정하기 위한 dto.
 *
 * @author seungyeon
 * @since 2023.08.01
 */
@Getter
@Setter
public class UpdateAccountRankDto {
    private Long accountId;
    private String rankCode;
}
