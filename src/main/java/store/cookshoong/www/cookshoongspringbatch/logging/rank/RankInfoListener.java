package store.cookshoong.www.cookshoongspringbatch.logging.rank;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongspringbatch.common.CommonProperties;
import store.cookshoong.www.cookshoongspringbatch.rank.dto.RankDto;

/**
 * 등급 정보 에러 발생 리스너.
 *
 * @author seungyeon
 * @since 2023.08.31
 */
@Component
@RequiredArgsConstructor
public class RankInfoListener {
    private static final Logger log = LoggerFactory.getLogger("store.cookshoong.www.cookshoongspringbatch.rank");
    private final CommonProperties commonProperties;

    /**
     * On read error.
     *
     * @param e the e
     */
    @OnReadError
    public void onReadError(Exception e) {
        log.error("[RankInfoStep] Reader : rankInfoReader, Error : {}", e.getMessage());
    }

    /**
     * On writer error.
     *
     * @param e     the e
     * @param items the items
     */
    @OnWriteError
    public void onWriterError(Exception e, List<RankDto> items) {
        for (RankDto item : items) {
            log.error("[RankInfoStep] Writer : rankInfoWriter, Error At : {}, Error : {}", item.getRankCode(), e.getMessage());
            try {
                String filePath = commonProperties.getRankInfoFile();
                String fileName = String.format("%s/error_%s.csv", filePath, LocalDate.now());

                CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName, true));
                String[] data = {item.getRankCode(), item.getCouponPolicyId().toString(), item.getUsagePeriod().toString()};
                csvWriter.writeNext(data);

                csvWriter.close();
            } catch (Exception exception) {
                log.error("[RankInfoStep] CsvWriter At OnWriteError : {}", exception.getMessage());
            }
        }
    }
}
