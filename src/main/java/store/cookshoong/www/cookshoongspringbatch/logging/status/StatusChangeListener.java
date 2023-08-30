package store.cookshoong.www.cookshoongspringbatch.logging.status;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;
import store.cookshoong.www.cookshoongspringbatch.status.dto.AccountStatusDto;

/**
 * 상태 변경시 reader, writer 실행 중 에러 발생하거나 skip하게 되는 경우에 log 파일에 기록.
 *
 * @author seungyeon
 * @since 2023.08.04
 */
@Component
public class StatusChangeListener {
    private static final Logger log = LoggerFactory.getLogger("store.cookshoong.www.cookshoongspringbatch.status");
    private final CommonProperties commonProperties;

    /**
     * Instantiates a new Status change skip listener.
     *
     * @param commonProperties the common properties
     */
    public StatusChangeListener(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    /**
     * On read error.
     *
     * @param e the e
     */
    @OnReadError
    public void onReadError(Exception e) {
        log.error("[StatusChangeStep] Reader : accountStatusReader, Error : {}", e.getMessage());
    }

    /**
     * On writer error.
     *
     * @param e     the e
     * @param items the items
     */
    @OnWriteError
    public void onWriterError(Exception e, List<AccountStatusDto> items) {
        for (AccountStatusDto item : items) {
            log.error("[StatusChangeStep] Writer : accountStatusWriter, Error At : {}, Error : {}", item.getAccountId(), e.getMessage());
            try {
                String filePath = commonProperties.getStatusFile();
                String fileName = String.format("%s/error_%s.csv", filePath, LocalDate.now());

                CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
                String[] data = {item.getAccountId().toString(), item.getLastLoginAt().toString(), item.getStatusCode()};
                csvWriter.writeNext(data);

                csvWriter.close();
            } catch (Exception exception) {
                log.error("[StatusChange] CsvWriter At OnWriteError : {}", exception.getMessage());
            }
        }
    }
}
