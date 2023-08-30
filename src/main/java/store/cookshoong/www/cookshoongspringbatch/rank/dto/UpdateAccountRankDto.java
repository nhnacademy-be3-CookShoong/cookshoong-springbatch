package store.cookshoong.www.cookshoongspringbatch.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원의 등급을 재설정하기 위한 dto.
 *
 * @author seungyeon
 * @since 2023.08.01
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRankDto {
    private Long accountId;
    private String rankCode;

    public void modify(Long accountId, String rankCode) {
        this.accountId = accountId;
        this.rankCode = rankCode;
    }
}
