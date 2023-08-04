package store.cookshoong.www.cookshoongspringbatch.rank.processor;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountOrderDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.UpdateAccountRankDto;

/**
 * 전달의 회원 주문 횟수를 기준으로 회원 등급 갱신.
 *
 * @author seungyeon
 * @since 2023.08.01
 */
@Slf4j
@StepScope
@Component
public class AccountRankProcessor implements ItemProcessor<SelectAccountOrderDto, UpdateAccountRankDto> {
    private List<RankDto> rankCodes;

    @Override
    public UpdateAccountRankDto process(SelectAccountOrderDto selectAccountOrderDto) throws Exception {
        UpdateAccountRankDto updateAccountRankDto = new UpdateAccountRankDto();
        updateAccountRankDto.setAccountId(selectAccountOrderDto.getAccountId());
        int orderCnt = selectAccountOrderDto.getOrderCnt();
        int[] rankStandardNum = {2, 5, 10};
        String rankCode = "LEVEL_4"; // 기본 값 LEVEL_4(VIP) 등급코드로

        for (int i = 0; i < rankStandardNum.length; i++) {
            if (orderCnt < rankStandardNum[i]) {
                rankCode = rankCodes.get(i).getRankCode();
                break;
            }
        }
        updateAccountRankDto.setRankCode(rankCode);

        log.info("AccountId : {}, RankCode : {}", updateAccountRankDto.getAccountId(), updateAccountRankDto.getRankCode());
        return updateAccountRankDto;
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
