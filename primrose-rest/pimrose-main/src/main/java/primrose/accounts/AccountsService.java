package primrose.accounts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountsService {

  private final AccountsRepository accountsRepository;

  public AccountsService(final AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  @Transactional
  public Account save(final Account account) {
    final long id = accountsRepository.getNewId();
    account.setId(id);
    accountsRepository.insert(account);

    return accountsRepository.getById(id);
  }

}
