package store.cookshoong.www.cookshoongspringbatch.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.07.31
 */
@Slf4j
@Component
public class LoggingListener {

    /**
     * Step 실행전 확인
     *
     * @param execution the execution
     */
    @BeforeStep
    public void beforeStep(StepExecution execution) {
        log.info("start : {}", execution.getStepName());
    }

    /**
     * Step 실행 후 확인
     *
     * @param execution the execution
     */
    @AfterStep
    public void afterStep(StepExecution execution) {
        log.info("end : {}", execution.getStepName());

    }
}
