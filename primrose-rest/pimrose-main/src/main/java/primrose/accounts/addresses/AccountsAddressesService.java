package primrose.accounts.addresses;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountsAddressesService {

  private final AccountsAddressesRepository accountsAddressesRepository;

  public AccountsAddressesService(final AccountsAddressesRepository accountsAddressesRepository) {
    this.accountsAddressesRepository = accountsAddressesRepository;
  }

  @Transactional
  public void save(final String accountCode, final String addressType, final long addressId) {
    accountsAddressesRepository.insert(addressType, addressId, accountCode);
  }

  @Transactional
  public void save(final long accountId, final String addressType, final long addressId) {
    accountsAddressesRepository.insert(addressType, addressId, accountId);
  }
}
