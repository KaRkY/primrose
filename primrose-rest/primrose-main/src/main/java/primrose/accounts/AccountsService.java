package primrose.accounts;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.addresses.AddressesRepository;
import primrose.contacts.ContactsRepository;
import primrose.util.IdUtil;

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
    if (!accountsRepository.typeExists(account.type())) {
      throw new IllegalArgumentException("Wrong account type.");
    }

    final long accountId = accountsRepository.nextValAccounts();

    accountsRepository
      .insert(accountId, account, SecurityContextHolder.getContext().getAuthentication().getName());

    return accountsRepository
      .loadById(accountId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s",
          Long.toString(IdUtil.pseudo(accountId), 36))));
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
    contactsRepository
      .insert(accountId, contactId, addressType, SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public Account loadByName(final String accountName) {
    return accountsRepository
      .loadByName(accountName)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s",
          accountName)));
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public Account loadById(final long accountId) {
    return accountsRepository
      .loadById(accountId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s",
          Long.toString(IdUtil.pseudo(accountId), 36))));
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public List<Account> loadBySearch(final AccountsSearch accountSearch) {
    return accountsRepository
      .loadBySearch(accountSearch);
  }
}
