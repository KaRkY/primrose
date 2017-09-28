package primrose.accounts.contacts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountsContactsService {

  private final AccountsContactsRepository accountsContactsRepository;

  public AccountsContactsService(final AccountsContactsRepository accountsContactsRepository) {
    this.accountsContactsRepository = accountsContactsRepository;
  }

  @Transactional
  public void save(final String accountCode, final String addressType, final long contactId) {
    accountsContactsRepository.insert(addressType, contactId, accountCode);
  }

  @Transactional
  public void save(final long accountId, final String addressType, final long contactId) {
    accountsContactsRepository.insert(addressType, contactId, accountId);
  }

  @Transactional
  public void save(final String accountCode, final String addressType, final String contactCode) {
    accountsContactsRepository.insert(addressType, contactCode, accountCode);
  }

  @Transactional
  public void save(final long accountId, final String addressType, final String contactCode) {
    accountsContactsRepository.insert(addressType, contactCode, accountId);
  }
}
