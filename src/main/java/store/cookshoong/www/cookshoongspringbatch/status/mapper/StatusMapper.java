package store.cookshoong.www.cookshoongspringbatch.status.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import store.cookshoong.www.cookshoongspringbatch.status.AccountStatusDto;

/**
 * {설명을 작성해주세요}.
 *
 * @author seungyeon
 * @since 2023.07.31
 */
@Mapper
public interface StatusMapper {
    /**
     * 회원 상태 변경을 위해 회원 정보를 가져오는 메서드.
     *
     * @return the accounts
     */
    @Select("select a.account_id, a.last_login_at, a.status_code from accounts as a")
    List<AccountStatusDto> getAccounts();

    @Update("update accounts as a set a.status_code = 'DORMANCY' where a.account_id= #{_accountStatusDto.accountId}")
    void updateAccounts(AccountStatusDto accountStatusDto);
}
