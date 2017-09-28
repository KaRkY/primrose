package primrose.accounts;

import org.springframework.security.core.context.SecurityContextHolder;
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
    final long accountId = accountsRepository.nextValAccounts();
    accountsRepository.insert(accountId, account, SecurityContextHolder.getContext().getAuthentication().getName());
    return accountsRepository.loadById(accountId);
  }

}
