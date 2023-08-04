package store.cookshoong.www.cookshoongspringbatch.status.mapper;

import java.util.List;
import store.cookshoong.www.cookshoongspringbatch.status.dto.AccountStatusDto;

/**
 * 회원 상태 변경에 대한 mapper.
 *
 * @author seungyeon
 * @since 2023.07.31
 */
public interface StatusMapper {
    /**
     * 회원 상태 변경을 위해 회원 정보를 가져오는 메서드.
     *
     * @return the accounts
     */
    List<AccountStatusDto> getAccounts();

    /**
     * Update accounts.
     */
    void updateAccounts();
}
