package store.cookshoong.www.cookshoongspringbatch.logging;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;
import store.cookshoong.www.cookshoongspringbatch.status.dto.AccountStatusDto;

/**
 * 회원 상태변경 Step에서 Skip된 item 파일로 기록.
 *
 * @author seungyeon
 * @since 2023.08.28
 */
@Component
public class StatusChangeSkipListener implements SkipListener<AccountStatusDto, AccountStatusDto> {
    private static final Logger log = LoggerFactory.getLogger("store.cookshoong.www.cookshoongspringbatch.status");

    private final CommonProperties commonProperties;

    /**
     * Instantiates a new Status change skip listener.
     *
     * @param commonProperties the common properties
     */
    public StatusChangeSkipListener(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    @Override
    public void onSkipInRead(Throwable t) {
        log.error("[StatusChangeStep] Reader Skip Reason : {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(AccountStatusDto item, Throwable t) {
        log.error("[StatusChangeStep] Writer Skip Reason : " + t.getMessage() + ", Skip Item : " + item.getAccountId());

        try {
            String filePath = commonProperties.getStatusFile();
            String fileName = String.format("%s/skip_%s.csv", filePath, LocalDate.now());

            CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
            String[] data = {item.getAccountId().toString(), item.getLastLoginAt().toString(), item.getStatusCode()};
            csvWriter.writeNext(data);

            csvWriter.close();
        } catch (Exception e) {
            log.error("[StatusChange] Skip Writer Error : {}", e.getMessage());
        }
    }

    @Override
    public void onSkipInProcess(AccountStatusDto item, Throwable t) {
        log.error("[StatusChangeStep] Process Skip Reason : {}", item.getAccountId(), t);
    }
}
