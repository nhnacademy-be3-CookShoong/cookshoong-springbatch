package store.cookshoong.www.cookshoongspringbatch.birthday;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 생일 쿠폰 정책 id.
 *
 * @author seungyeon
 * @since 2023.08.23ㅌ
 */
@Getter
@ConfigurationProperties(prefix = "cookshoong")
public class BirthdayProperties {
    private Long birthdayPolicy;
}
