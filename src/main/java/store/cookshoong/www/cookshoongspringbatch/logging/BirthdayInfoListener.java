package store.cookshoong.www.cookshoongspringbatch.logging;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.BirthdayCouponInfoDto;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;
import store.cookshoong.www.cookshoongspringbatch.status.dto.AccountStatusDto;

/**
 * 생일쿠폰 정보 Error Listener.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Component
public class BirthdayInfoListener {
    private static final Logger log = LoggerFactory.getLogger("store.cookshoong.www.cookshoongspringbatch.birthday");
    private final CommonProperties commonProperties;

    /**
     * Instantiates a new Status change skip listener.
     *
     * @param commonProperties the common properties
     */
    public BirthdayInfoListener(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    /**
     * On read error.
     *
     * @param e the e
     */
    @OnReadError
    public void onReadError(Exception e) {
        log.error("[BirthdayInfoStep] Reader : birthdayInfoReader, Error : {}", e.getMessage());
    }

    /**
     * On writer error.
     *
     * @param e     the e
     * @param items the items
     */
    @OnWriteError
    public void onWriterError(Exception e, List<BirthdayCouponInfoDto> items) {
        for (BirthdayCouponInfoDto item : items) {
            log.error("[BirthdayInfoStep] Writer : birthdayInfoWriter, Error At : {}, Error : {}", item.getCouponPolicyId(), e.getMessage());
            try {
                String filePath = commonProperties.getBirthdayInfoFile();
                String fileName = String.format("%s/error_%s.csv", filePath, LocalDate.now());

                CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
                String[] data = {item.getCouponPolicyId().toString(), item.getUsagePeriod().toString()};
                csvWriter.writeNext(data);

                csvWriter.close();
            } catch (Exception exception) {
                log.error("[BirthdayInfo] CsvWriter At OnWriteError : {}", exception.getMessage());
            }
        }
    }
}
