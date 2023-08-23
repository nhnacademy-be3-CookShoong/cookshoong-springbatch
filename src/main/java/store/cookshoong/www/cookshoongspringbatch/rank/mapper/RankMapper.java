package store.cookshoong.www.cookshoongspringbatch.rank.mapper;

import java.util.List;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountOrderDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;

/**
 * 등급 재산정을 위한 Mapper.
 *
 * @author seungyeon
 * @since 2023.08.01
 */
public interface RankMapper {
    List<RankDto> selectRankCodes();
    List<SelectAccountOrderDto> selectOrderCntByAccount();
    void updateRank();
    void insertRankCoupon();
}
