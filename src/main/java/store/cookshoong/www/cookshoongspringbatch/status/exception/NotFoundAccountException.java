package store.cookshoong.www.cookshoongspringbatch.status.exception;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.07.31
 */
public class NotFoundAccountException extends RuntimeException{
    public NotFoundAccountException(){
        super("해당 회원은 존재하지 않습니다.");
    }
}
