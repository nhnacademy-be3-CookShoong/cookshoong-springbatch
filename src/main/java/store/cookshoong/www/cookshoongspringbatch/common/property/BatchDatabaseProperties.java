package store.cookshoong.www.cookshoongspringbatch.common.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.07.30
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchDatabaseProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
