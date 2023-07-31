package store.cookshoong.www.cookshoongspringbatch.status;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.07.30
 */
@Getter
@NoArgsConstructor
public class AccountStatusDto {
    private Long accountId;
    private LocalDateTime lastLoginAt;
    @Setter
    private String statusCode;
}

