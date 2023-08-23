package store.cookshoong.www.cookshoongspringbatch.rank.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

/**
 * 등급에 대한 리스트를 가져올 때 사용하는 등급 dto.
 *
 * @author seungyeon (유승연)
 * @since 2023.08.03
 */
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class RankDto {
    private String rankCode;
    private Long couponPolicyId;
    private Integer usagePeriod;
}
