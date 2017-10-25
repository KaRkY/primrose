package primrose.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.data.AccountsRepository;
import primrose.model.input.BaseInputAccount;
import primrose.model.input.BaseInputAccountAddress;
import primrose.model.input.BaseInputAccountContact;
import primrose.model.output.BaseOutputAccount;
import primrose.model.output.BaseOutputAccountAddressType;
import primrose.model.output.BaseOutputAccountContactType;
import primrose.model.output.BaseOutputAccountType;
import primrose.model.sort.Sort;

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
  public String addAddress(final String accountId, final BaseInputAccountAddress address) {
    final String addressId = addressesService.save(address);
    accountsRepository.assignAddress(
      accountId,
      addressId,
      address.type());
    return addressId;
  }

  @Transactional
  public String addContact(final String accountId, final BaseInputAccountContact contact) {
    final String contactId = contactsService.save(contact);
    accountsRepository.assignContact(
      accountId,
      contactId,
      contact.type());
    return contactId;
  }

  public int addressCount(final String account) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Transactional(readOnly = true)
  public int count() {
    return accountsRepository.count();
  }

  @Transactional
  public String create(final BaseInputAccount account) {
    if (!accountsRepository
      .typeExists(account.type())) throw new IllegalArgumentException("Wrong account type: " + account.type());

    final String accountId = getNextId();

    accountsRepository
      .insert(
        accountId,
        account);

    account
      .addresses()
      .forEach(address -> addAddress(accountId, address));

    account
      .contacts()
      .forEach(contact -> addContact(accountId, contact));

    return accountId;
  }

  @Transactional
  public String getNextId() {
    return accountsRepository.nextValAccounts();
  }

  @Transactional(readOnly = true)
  public List<BaseOutputAccount> load(final Integer page, final Integer size, final Sort sort) {
    return accountsRepository.load(page, size, sort);
  }

  @Transactional(readOnly = true)
  @Cacheable("accountAddressTypes")
  public BaseOutputAccountAddressType loadAddressType(final String type) {
    return accountsRepository
      .loadAddressType(type)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account address type %s", type)));
  }

  @Transactional(readOnly = true)
  @Cacheable("accountAddressTypes")
  public List<BaseOutputAccountAddressType> loadAddressTypes() {
    return accountsRepository.loadAddressTypes();
  }

  @Transactional(readOnly = true)
  public BaseOutputAccount loadById(final String accountId) {
    return accountsRepository
      .loadById(accountId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s", accountId)));
  }

  @Transactional(readOnly = true)
  public BaseOutputAccount loadByName(final String accountName) {
    return accountsRepository
      .loadByName(accountName)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account %s",
          accountName)));
  }

  @Transactional(readOnly = true)
  @Cacheable("accountContactTypes")
  public BaseOutputAccountContactType loadContactType(final String type) {
    return accountsRepository
      .loadContactType(type)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account contact type %s", type)));
  }

  @Transactional(readOnly = true)
  @Cacheable("accountContactTypes")
  public List<BaseOutputAccountContactType> loadContactTypes() {
    return accountsRepository.loadContactTypes();
  }

  @Transactional(readOnly = true)
  @Cacheable("accountTypes")
  public BaseOutputAccountType loadType(final String type) {
    return accountsRepository
      .loadType(type)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account type %s", type)));
  }

  @Transactional(readOnly = true)
  @Cacheable("accountTypes")
  public List<BaseOutputAccountType> loadTypes() {
    return accountsRepository.loadTypes();
  }

  @Transactional
  public String update(final String accountId, final BaseInputAccount account) {
    if (!accountsRepository
      .typeExists(account.type())) throw new IllegalArgumentException("Wrong account type: " + account.type());

    accountsRepository
      .update(
        accountId,
        account);

    return accountId;
  }
}
