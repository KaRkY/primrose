package primrose.accounts;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.addresses.AddressesRepository;
import primrose.contacts.ContactsRepository;

@Service
public class AccountsService {

  private final AccountsRepository accountsRepository;
  private final AddressesRepository addressesRepository;
  private final ContactsRepository contactsRepository;

  public AccountsService(
    final AccountsRepository accountsRepository,
    final AddressesRepository addressesRepository,
    final ContactsRepository contactsRepository) {
    this.accountsRepository = accountsRepository;
    this.addressesRepository = addressesRepository;
    this.contactsRepository = contactsRepository;
  }

  @Transactional
  @Secured({ "accounts:create" })
  public Account save(final Account account) {
    final long accountId = accountsRepository.nextValAccounts();
    accountsRepository.insert(accountId, account, SecurityContextHolder.getContext().getAuthentication().getName());
    return accountsRepository.loadById(accountId);
  }

  @Transactional
  @Secured({ "account_addresses:create" })
  public void addAddress(final long accountId, final long addressId, final String addressType) {
    addressesRepository.insert(accountId, addressId, addressType,
      SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @Transactional
  @Secured({ "account_contacts:create" })
  public void addContact(final long accountId, final long contactId, final String addressType) {
    contactsRepository.insert(accountId, contactId, addressType,
      SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public Account loadByName(final String accountName) {
    return accountsRepository.loadByName(accountName);
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public Account loadById(final long accountId) {
    return accountsRepository.loadById(accountId);
  }
}
