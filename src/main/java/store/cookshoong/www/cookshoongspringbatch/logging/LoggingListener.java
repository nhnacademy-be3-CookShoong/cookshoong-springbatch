package store.cookshoong.www.cookshoongspringbatch.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

/**
 * Step 전후로 확인하는 listener.
 *
 * @author seungyeon
 * @since 2023.08.03
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
    public void beforeStep(StepExecution execution){
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
