package primrose.accounts;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.addresses.Address;
import primrose.addresses.AddressSearchParameters;
import primrose.addresses.AddressesService;
import primrose.contacts.Contact;
import primrose.contacts.ContactSearchParameters;
import primrose.contacts.ContactsService;
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

  @Transactional(readOnly = true)
  public String getNextId() {
    return accountsRepository.nextValAccounts();
  }

  @Transactional
  @Secured({ "accounts:create" })
  public Account save(final Account account) {
    if (!accountsRepository.typeExists(account.type())) { throw new IllegalArgumentException("Wrong account type: " + account.type()); }

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
  public List<Account> loadBySearch(
    final AccountSearchParameters account,
    final AddressSearchParameters address,
    final ContactSearchParameters contact,
    final Integer page,
    final Integer size,
    final Sort sort) {
    return accountsRepository
      .loadBySearch(
        account,
        address,
        contact,
        page,
        size,
        sort);
  }
}
