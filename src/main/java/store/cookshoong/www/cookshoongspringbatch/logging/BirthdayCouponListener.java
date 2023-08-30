package store.cookshoong.www.cookshoongspringbatch.logging;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.InsertIssueCouponDto;
import store.cookshoong.www.cookshoongspringbatch.birthday.dto.SelectAccountDto;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;

/**
 * 쿠폰 발급 ErrorListener.
 *
 * @author seungyeon
 * @since 2023.08.30
 */
@Component
public class BirthdayCouponListener {
    private static final Logger log = LoggerFactory.getLogger("store.cookshoong.www.cookshoongspringbatch.birthday");
    private final CommonProperties commonProperties;

    /**
     * Instantiates a new Status change skip listener.
     *
     * @param commonProperties the common properties
     */
    public BirthdayCouponListener(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    /**
     * On read error.
     *
     * @param e the e
     */
    @OnReadError
    public void onReadError(Exception e) {
        log.error("[BirthdayCouponStep] Reader : birthdayIssueCouponReader, Error : {}", e.getMessage());
    }

    /**
     * On process error.
     *
     * @param e    the e
     * @param item the item
     */
    @OnProcessError
    public void onProcessError(Exception e, SelectAccountDto item) {
        log.error("[BirthdayIssueCoupon] Processor : birthdayIssueCouponProcessor Error At : {}, Error : {}", item.getAccountId(), e.getMessage());
        try {
            String filePath = commonProperties.getBirthdayCouponFile();
            String fileName = String.format("%s/error_%s.csv", filePath, LocalDate.now());

            CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
            String[] data = {item.getAccountId().toString()};
            csvWriter.writeNext(data);

            csvWriter.close();
        } catch (Exception exception) {
            log.error("[BirthdayCouponStep] CsvWriter At OnWriteError : {}", exception.getMessage());
        }
    }

    /**
     * On writer error.
     *
     * @param e     the e
     * @param items the items
     */
    @OnWriteError
    public void onWriterError(Exception e, List<InsertIssueCouponDto> items) {
        for (InsertIssueCouponDto item : items) {
            log.error("[BirthdayCouponStep] Writer : birthdayIssueCouponWriter, Error At : {}, Error : {}", item.getAccountId(), e.getMessage());
            try {
                String filePath = commonProperties.getBirthdayCouponFile();
                String fileName = String.format("%s/error_%s.csv", filePath, LocalDate.now());

                CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
                String[] data = {item.getAccountId().toString(), item.getCouponPolicyId().toString(), item.getReceiptDate().toString(), item.getExpirationDate().toString()};
                csvWriter.writeNext(data);

                csvWriter.close();
            } catch (Exception exception) {
                log.error("[BirthdayCouponStep] CsvWriter At OnWriteError : {}", exception.getMessage());
            }
        }
    }
}
