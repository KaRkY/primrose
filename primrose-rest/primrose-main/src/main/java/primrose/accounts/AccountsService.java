package primrose.accounts;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.addresses.AddressesService;
import primrose.contacts.ContactsService;
import primrose.model.BaseInputAccount;
import primrose.model.BaseInputAccountAddress;
import primrose.model.BaseInputAccountContact;
import primrose.model.BaseOutputAccount;
import primrose.pagging.sort.Sort;

@Service
public class AccountsService {

  private final AccountsRepository accountsRepository;
  private final AddressesService addressesService;
  private final ContactsService contactsService;

  public AccountsService(
    final AccountsRepository accountsRepository,
    final AddressesService addressesService,
    final ContactsService contactsService) {
    this.accountsRepository = accountsRepository;
    this.addressesService = addressesService;
    this.contactsService = contactsService;
  }

  @Transactional
  public String getNextId() {
    return accountsRepository.nextValAccounts();
  }

  @Transactional
  @Secured({ "accounts:create" })
  public String create(final BaseInputAccount account) {
    if (!accountsRepository
      .typeExists(account.type())) { throw new IllegalArgumentException("Wrong account type: " + account.type()); }

    final String accountId = getNextId();

    accountsRepository
      .insert(
        accountId,
        account,
        SecurityContextHolder.getContext().getAuthentication().getName());

    account
      .addresses()
      .forEach(address -> addAddress(accountId, address));

    account
      .contacts()
      .forEach(contact -> addContact(accountId, contact));

    return accountId;
  }

  @Transactional
  @Secured({ "accounts:update" })
  public String update(final String accountId, final BaseInputAccount account) {
    if (!accountsRepository
      .typeExists(account.type())) { throw new IllegalArgumentException("Wrong account type: " + account.type()); }

    accountsRepository
      .update(
        accountId,
        account,
        SecurityContextHolder.getContext().getAuthentication().getName());

    return accountId;
  }

  @Transactional
  @Secured({ "account_addresses:create" })
  public String addAddress(final String accountId, final BaseInputAccountAddress address) {
    final String addressId = addressesService.save(address);
    accountsRepository.assignAddress(
      accountId,
      addressId,
      address.type(),
      SecurityContextHolder.getContext().getAuthentication().getName());
    return addressId;
  }

  @Transactional
  @Secured({ "account_contacts:create" })
  public String addContact(final String accountId, final BaseInputAccountContact contact) {
    final String contactId = contactsService.save(contact);
    accountsRepository.assignContact(
      accountId,
      contactId,
      contact.type(),
      SecurityContextHolder.getContext().getAuthentication().getName());
    return contactId;
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public BaseOutputAccount loadByName(final String accountName) {
    return accountsRepository
      .loadByName(accountName)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s",
          accountName)));
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public BaseOutputAccount loadById(final String accountId) {
    return accountsRepository
      .loadById(accountId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s", accountId)));
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public int count() {
    return accountsRepository.count();
  }

  @Transactional(readOnly = true)
  @Secured({ "accounts:read" })
  public List<BaseOutputAccount> load(final Integer page, final Integer size, final Sort sort) {
    return accountsRepository.load(page, size, sort);
  }

  @Transactional(readOnly = true)
  @Cacheable("accountTypes")
  public List<AccountType> loadTypes() {
    return accountsRepository.loadTypes();
  }

  @Transactional(readOnly = true)
  @Cacheable("accountTypes")
  public AccountType loadType(final String type) {
    return accountsRepository
      .loadType(type)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account type %s", type)));
  }

  @Transactional(readOnly = true)
  @Cacheable("accountAddressTypes")
  public List<AccountAddressType> loadAddressTypes() {
    return accountsRepository.loadAddressTypes();
  }

  @Transactional(readOnly = true)
  @Cacheable("accountAddressTypes")
  public AccountAddressType loadAddressType(final String type) {
    return accountsRepository
      .loadAddressType(type)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account address type %s", type)));
  }

  @Transactional(readOnly = true)
  @Cacheable("accountContactTypes")
  public List<AccountContactType> loadContactTypes() {
    return accountsRepository.loadContactTypes();
  }

  @Transactional(readOnly = true)
  @Cacheable("accountContactTypes")
  public AccountContactType loadContactType(final String type) {
    return accountsRepository
      .loadContactType(type)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account contact type %s", type)));
  }

  public int addressCount(final String account) {
    // TODO Auto-generated method stub
    return 0;
  }
}
