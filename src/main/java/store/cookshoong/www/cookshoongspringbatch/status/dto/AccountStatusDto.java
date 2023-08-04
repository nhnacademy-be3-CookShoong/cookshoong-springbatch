package store.cookshoong.www.cookshoongspringbatch.status.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 정보를 담는 dto.
 *
 * @author seungyeon
 * @since 2023.07.30
 */
@Getter
public class AccountStatusDto {
    private Long accountId;
    private LocalDateTime lastLoginAt;
    @Setter
    private String statusCode;
}
