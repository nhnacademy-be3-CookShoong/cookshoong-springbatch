package store.cookshoong.www.cookshoongspringbatch.logging.birthday;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.BirthdayCouponInfoDto;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;
import store.cookshoong.www.cookshoongspringbatch.status.dto.AccountStatusDto;

/**
 * 생일쿠폰 정보 Skip Listener.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Component
public class BirthdayInfoSkipListener implements SkipListener<BirthdayCouponInfoDto, BirthdayCouponInfoDto> {
    private static final Logger log = LoggerFactory.getLogger("store.cookshoong.www.cookshoongspringbatch.birthday");

    private final CommonProperties commonProperties;

    /**
     * Instantiates a new Status change skip listener.
     *
     * @param commonProperties the common properties
     */
    public BirthdayInfoSkipListener(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    @Override
    public void onSkipInRead(Throwable t) {
        log.error("[StatusChangeStep] Reader Skip Reason : {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(BirthdayCouponInfoDto item, Throwable t) {
        log.error("[StatusChangeStep] Writer Skip Reason : " + t.getMessage() + ", Skip Item : " + item.getCouponPolicyId());

        try {
            String filePath = commonProperties.getBirthdayInfoFile();
            String fileName = String.format("%s/skip_%s.csv", filePath, LocalDate.now());

            CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
            String[] data = {item.getCouponPolicyId().toString(), item.getUsagePeriod().toString()};
            csvWriter.writeNext(data);

            csvWriter.close();
        } catch (Exception e) {
            log.error("[BirthdayInfoStep] Skip Writer Error : {}", e.getMessage());
        }
    }

    @Override
    public void onSkipInProcess(BirthdayCouponInfoDto item, Throwable t) {
        log.error("[BirthdayInfoStep] Process Skip Reason : {}", item.getCouponPolicyId(), t);
    }
}

