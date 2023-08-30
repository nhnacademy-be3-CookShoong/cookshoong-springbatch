package store.cookshoong.www.cookshoongspringbatch.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.08.28
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "cookshoong")
public class CommonProperties {
    private String hookUrl;
    private Long birthdayPolicy;
    private String statusFile;
    private String birthdayInfoFile;
    private String birthdayCouponFile;
    private String rankInfoFile;
    private String accountRankFile;
    private String rankIssueFile;
}
