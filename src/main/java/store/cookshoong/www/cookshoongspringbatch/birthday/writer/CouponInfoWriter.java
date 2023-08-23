package store.cookshoong.www.cookshoongspringbatch.birthday.writer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.BirthdayCouponInfoDto;

/**
 * 생일 쿠폰에 대한 정보를 가져와서 DatashareBean 에 등록.
 * 생일 쿠폰의 정책 아이디와 사용기간을 가져옴.
 *
 * @author seungyeon
 * @since 2023.08.15
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@StepScope
public class CouponInfoWriter implements ItemWriter<BirthdayCouponInfoDto> {
    //private final DataShareBean<BirthdayCouponInfoDto> dataShareBean;
    private StepExecution stepExecution;

    private static final Logger logger = LoggerFactory.getLogger(CouponInfoWriter.class);

    @Override
    public void write(List<? extends BirthdayCouponInfoDto> list) throws Exception {
        logger.info("BirthdayCoupon info 시작");
        ExecutionContext executionContext = this.stepExecution.getExecutionContext();
        executionContext.put("birthdayCoupon", list.get(0));
        //BirthdayCouponInfoDto birthdayCouponInfoDto = list.get(0);
        //logger.info("BirthdayCoupon info 정보 : {}", birthdayCouponInfoDto);
        //dataShareBean.putData("BIRTHDAY_COUPON", birthdayCouponInfoDto);
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        log.warn("다음 read 실행 전 시작됨 : {}", stepExecution.getJobExecution().getExecutionContext().get("birthdayCoupon"));
    }
}
