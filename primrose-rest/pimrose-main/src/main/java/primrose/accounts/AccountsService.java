package primrose.accounts;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.util.Mod;

@Service
public class AccountsService {

  private final AccountsRepository accountsRepository;

  public AccountsService(final AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  @Transactional
  public Account save(final Account account) {
    String id = String.valueOf(accountsRepository.getNewCode());
    int checkDigit = Mod.mod11(id);

    while (checkDigit > 9) {
      id = String.valueOf(accountsRepository.getNewCode());
      checkDigit = Mod.mod11(id);
    }

    final String code = new StringBuilder()
      .append("A-")
      .append(StringUtils.leftPad(id, 10, '0'))
      .append(Mod.mod11(id))
      .toString();

    account.setCode(code);

    accountsRepository.insert(account);

    return accountsRepository.getByCode(code);
  }

}
