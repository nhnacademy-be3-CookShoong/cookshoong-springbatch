package store.cookshoong.www.cookshoongspringbatch.common.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SKM 에서 DB 정보 가져오기 위한 Dto.
 *
 * @author seungyeon (유승연)
 * @since 2023.07.30
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DatabaseProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
