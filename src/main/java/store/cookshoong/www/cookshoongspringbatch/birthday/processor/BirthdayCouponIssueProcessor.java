package store.cookshoong.www.cookshoongspringbatch.birthday.processor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.DataShareBean;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.BirthdayCouponInfoDto;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.SelectAccountDto;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.InsertIssueCouponDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.08.15
 */
@Slf4j
@StepScope
@Component
public class BirthdayCouponIssueProcessor implements ItemProcessor<SelectAccountDto, InsertIssueCouponDto> {
    private BirthdayCouponInfoDto birthdayCoupon;
    private static Logger logger = LoggerFactory.getLogger(BirthdayCouponIssueProcessor.class);
    private DataShareBean<BirthdayCouponInfoDto> dataShareBean;

    /**
     * Instantiates a new Birthday coupon issue processor.
     *
     * @param dataShareBean the data share bean
     */
    public BirthdayCouponIssueProcessor(DataShareBean<BirthdayCouponInfoDto> dataShareBean) {
        this.dataShareBean = dataShareBean;
    }

    @Override
    public InsertIssueCouponDto process(SelectAccountDto selectAccountDto) throws Exception {
        logger.info("BirthdayCoupon 발급을 위한 Processor");
        Long couponPolicyId = birthdayCoupon.getCouponPolicyId();
        int usagePeriod = birthdayCoupon.getUsagePeriod();
        LocalDate lastDate = LocalDate.now().plusDays(usagePeriod);
        return new InsertIssueCouponDto(selectAccountDto.getAccountId(), couponPolicyId, LocalDate.now(), lastDate);
    }

//    /**
//     * Retrieve inter step data.
//     *
//     * @param stepExecution the step execution
//     */
//    @BeforeStep
//    public void retrieveInterStepData(StepExecution stepExecution) {
//        this.birthdayCoupon = dataShareBean.getData("BIRTHDAY_COUPON");
//    }

    @BeforeStep
    public void reGet(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        this.birthdayCoupon = (BirthdayCouponInfoDto) jobContext.get("birthdayCoupon");

        log.warn("birthdayCoupon reGet : {}", birthdayCoupon);
    }
}
