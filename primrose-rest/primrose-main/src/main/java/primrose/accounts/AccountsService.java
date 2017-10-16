package primrose.accounts;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.addresses.Address;
import primrose.addresses.AddressesService;
import primrose.contacts.Contact;
import primrose.contacts.ContactsService;
import primrose.pagging.sort.Sort;

@Service
public class AccountsService {

  private final AccountsRepository accountsRepository;
  private final AddressesService   addressesService;
  private final ContactsService    contactsService;

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
  public Account create(final Account account) {
    if (!accountsRepository
      .typeExists(account.type())) { throw new IllegalArgumentException("Wrong account type: " + account.type()); }

    accountsRepository
      .insert(
        account,
        SecurityContextHolder.getContext().getAuthentication().getName());

    return accountsRepository
      .loadById(account.id())
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s", account.id())));
  }

  @Transactional
  @Secured({ "accounts:update" })
  public Account update(final String accountId, final Account account) {
    if (!accountsRepository
      .typeExists(account.type())) { throw new IllegalArgumentException("Wrong account type: " + account.type()); }

    accountsRepository
      .update(
        accountId,
        account,
        SecurityContextHolder.getContext().getAuthentication().getName());

    return accountsRepository
      .loadById(accountId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s", account.id())));
  }

  @Transactional
  @Secured({ "account_addresses:create" })
  public Address addAddress(final String accountId, final String addressId, final String addressType) {
    accountsRepository
      .assignAddress(
        accountId,
        addressId,
        addressType,
        SecurityContextHolder.getContext().getAuthentication().getName());
    return addressesService.loadById(addressId);
  }

  @Transactional
  @Secured({ "account_addresses:create" })
  public Address addAddress(final String accountId, final Address address, final String addressType) {
    final Address savedAddress = addressesService.save(address);
    addAddress(accountId, savedAddress.id(), addressType);
    return savedAddress;
  }

  @Transactional
  @Secured({ "account_contacts:create" })
  public Contact addContact(final String accountId, final String contactId, final String addressType) {
    accountsRepository
      .assignContact(
        accountId,
        contactId,
        addressType,
        SecurityContextHolder.getContext().getAuthentication().getName());
    return contactsService.loadById(contactId);
  }

  @Transactional
  @Secured({ "account_contacts:create" })
  public Contact addContact(final String accountId, final Contact contact, final String addressType) {
    final Contact savedContact = contactsService.save(contact);
    addContact(accountId, savedContact.id(), addressType);
    return savedContact;
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
  public Account loadById(final String accountId) {
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
  public List<Account> load(final Integer page, final Integer size, final Sort sort) {
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
