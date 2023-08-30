package store.cookshoong.www.cookshoongspringbatch.rank.processor;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.InsertRankCouponDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountOrderDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountRankCouponDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.UpdateAccountRankDto;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Slf4j
@StepScope
@Component
public class RankCouponProcessor implements ItemProcessor<SelectAccountRankCouponDto, InsertRankCouponDto> {
    @Override
    public InsertRankCouponDto process(SelectAccountRankCouponDto selectAccountRankCouponDto) throws Exception {
       log.error("============== RankCoupon Processor Start ===============");
        InsertRankCouponDto insertRankCouponDto = new InsertRankCouponDto();
        insertRankCouponDto.setAccountId(selectAccountRankCouponDto.getAccountId());
        insertRankCouponDto.setCouponPolicyId(selectAccountRankCouponDto.getCouponPolicyId());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(selectAccountRankCouponDto.getUsagePeriod());

        insertRankCouponDto.setStartDate(startDate);
        insertRankCouponDto.setEndDate(endDate);
        log.error("AccountId : {}, CouponPolicyId : {}", insertRankCouponDto.getAccountId(), insertRankCouponDto.getCouponPolicyId());
        return insertRankCouponDto;
    }
}
