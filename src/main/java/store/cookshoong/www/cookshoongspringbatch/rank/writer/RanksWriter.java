package store.cookshoong.www.cookshoongspringbatch.rank.writer;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;

/**
 * 등급 리스트 stepExecution에 저장.
 *
 * @author seungyeon
 * @since 2023.08.03
 */
@Slf4j
@Configuration
@StepScope
public class RanksWriter implements ItemWriter<RankDto> {
    private StepExecution stepExecution;

    @Override
    public void write(List<? extends RankDto> ranks) throws Exception {
        log.warn("==========Rank List Write Start==========");
        ExecutionContext executionContext = this.stepExecution.getExecutionContext();
        executionContext.put("rankCodes", ranks);
        log.warn("ranks : {}", ranks);
    }

    /**
     * StepExecution에 데이터를 저장.
     *
     * @param stepExecution the step execution
     */
    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        log.warn("다음 read 실행 전에 시작됨 : {}", stepExecution.getJobExecution().getExecutionContext().get("rankCodes"));
    }
}
