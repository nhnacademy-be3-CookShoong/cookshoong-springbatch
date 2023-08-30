package store.cookshoong.www.cookshoongspringbatch.logging.rank;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.SelectAccountOrderDto;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.UpdateAccountRankDto;

/**
 * 회원등급 갱신 중 에러 리스너.
 *
 * @author seungyeon
 * @since 2023.08.31
 */
@Component
@RequiredArgsConstructor
public class AccountRankListener {
    private static final Logger log = LoggerFactory.getLogger("store.cookshoong.www.cookshoongspringbatch.rank");
    private final CommonProperties commonProperties;

    /**
     * On read error.
     *
     * @param e the e
     */
    @OnReadError
    public void onReadError(Exception e) {
        log.error("[AccountRankStep] Reader : rankInfoReader, Error : {}", e.getMessage());
    }

    @OnProcessError
    public void onProcessError(SelectAccountOrderDto item, Exception e) {
        log.error("[AccountRankStep] Processor : accountRankProcessor Error At : {}, Error : {}", item.getAccountId(), e.getMessage());
        try {
            String filePath = commonProperties.getAccountRankFile();
            String fileName = String.format("%s/error_%s.csv", filePath, LocalDate.now());

            CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
            String[] data = {item.getAccountId().toString(), item.getOrderCnt().toString()};
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
    public void onWriterError(Exception e, List<UpdateAccountRankDto> items) {
        for (UpdateAccountRankDto item : items) {
            log.error("[AccountRankStep] Writer : accountRankWriter, Error At : {}, Error : {}", item.getRankCode(), e.getMessage());
            try {
                String filePath = commonProperties.getAccountRankFile();
                String fileName = String.format("%s/error_%s.csv", filePath, LocalDate.now());

                CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
                String[] data = {item.getAccountId().toString(), item.getRankCode()};
                csvWriter.writeNext(data);

                csvWriter.close();
            } catch (Exception exception) {
                log.error("[AccountRankStep] CsvWriter At OnWriteError : {}", exception.getMessage());
            }
        }
    }
}
