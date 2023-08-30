package store.cookshoong.www.cookshoongspringbatch.rank.processor;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.InsertRankCouponDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountRankCouponDto;

/**
 * 등급쿠폰 발급 프로세서.
 *
 * @author seungyeon
 * @since 2023.08.20
 */
@Slf4j
@StepScope
@Component
public class RankCouponProcessor implements ItemProcessor<SelectAccountRankCouponDto, InsertRankCouponDto> {

    private List<RankDto> rankCodes;

    @Override
    public InsertRankCouponDto process(SelectAccountRankCouponDto selectAccountRankCouponDto) throws Exception {
        log.info("============== RankCoupon Processor Start ===============");

        InsertRankCouponDto insertRankCouponDto = new InsertRankCouponDto();
        insertRankCouponDto.setAccountId(selectAccountRankCouponDto.getAccountId());

        for (int i = 0; i < rankCodes.size(); i++) {
            if (selectAccountRankCouponDto.getRankCode().equals(rankCodes.get(i).getRankCode())) {
                insertRankCouponDto.setCouponPolicyId(rankCodes.get(i).getCouponPolicyId());
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(rankCodes.get(i).getUsagePeriod());
                insertRankCouponDto.setStartDate(startDate);
                insertRankCouponDto.setEndDate(endDate);
                break;
            }
        }
        log.info("AccountId : {}, CouponPolicyId : {}", insertRankCouponDto.getAccountId(), insertRankCouponDto.getCouponPolicyId());
        return insertRankCouponDto;
    }

    /**
     * JobExecutionContext에 저장한 등급 리스트를 가져와서 List로 저장.
     *
     * @param stepExecution the step execution
     */
    @BeforeStep
    public void reGet(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        this.rankCodes = (List<RankDto>) jobContext.get("rankCodes");

        log.warn("rankCodes reGet : {}", rankCodes);
    }
}
