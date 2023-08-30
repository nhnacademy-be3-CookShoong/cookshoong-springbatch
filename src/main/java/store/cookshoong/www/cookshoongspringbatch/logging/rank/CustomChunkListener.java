package store.cookshoong.www.cookshoongspringbatch.logging.rank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

/**
 * 청크단위로 시간 계산 및 에러시 기록하는 리스너.
 *
 * @author seungyeon
 * @since 2023.08.31
 */
@Slf4j
@Component
public class CustomChunkListener implements ChunkListener {

    private long startTime;

    @Override
    public void beforeChunk(ChunkContext context) {
        startTime = System.currentTimeMillis(); // 청크 시작 시간 기록
    }

    @Override
    public void afterChunk(ChunkContext context) {
        long endTime = System.currentTimeMillis(); // 청크 종료 시간 기록
        long elapsedTime = endTime - startTime;
        log.error("Chunk processing took {} ms", elapsedTime);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        int pageNum = context.getStepContext().getStepExecution().getCommitCount(); // 현재 페이지 번호
        log.error("[ChunkListen] Error page at {}", pageNum);
    }
}
