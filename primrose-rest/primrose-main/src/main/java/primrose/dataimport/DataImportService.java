package primrose.dataimport;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.accounts.Account;
import primrose.accounts.AccountsService;
import primrose.accounts.ImmutableAccount;
import primrose.accounts.ImmutableAccount.Builder;
import primrose.accounts.addresses.AccountsAddressesService;
import primrose.accounts.contacts.AccountsContactsService;
import primrose.addresses.Address;
import primrose.addresses.AddressesService;
import primrose.contacts.Contact;
import primrose.contacts.ContactsService;

@Service
public class DataImportService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final AccountsService accountsService;
  private final AddressesService addressService;
  private final AccountsAddressesService accountsAddressesService;
  private final ContactsService contactsService;
  private final AccountsContactsService accountsContactsService;

  public DataImportService(
    final AccountsService accountsService,
    final AddressesService addressService,
    final AccountsAddressesService accountsAddressesService,
    final ContactsService contactsService,
    final AccountsContactsService accountsContactsService) {
    this.accountsService = accountsService;
    this.addressService = addressService;
    this.accountsAddressesService = accountsAddressesService;
    this.contactsService = contactsService;
    this.accountsContactsService = accountsContactsService;

  }

  @Transactional
  public List<Account> importAccounts(final List<Account> accounts) {
    final List<Account> result = new ArrayList<>();
    final int size = accounts.size();
    int current = 0;

    for (final Account account : accounts) {
      final Account savedAccount = accountsService.save(account);
      final Builder accountBuilder = ImmutableAccount.builder().from(savedAccount);

      account.addresses().forEach((key, value) -> {
        final Address address = addressService.save(value);
        accountsAddressesService.save(savedAccount.name(), key, address.code());
        accountBuilder.putAddresses(key, address);
      });

      account.contacts().forEach((key, value) -> {
        final Contact contact = contactsService.save(value);
        accountsContactsService.save(savedAccount.name(), key, contact.code());
        accountBuilder.putContacts(key, contact);
      });

      result.add(accountBuilder.build());
      current++;

      if (current % 100 == 0) {
        logger.info("Transfering accounts {}% done", current / (double) size * 100);
      }
    }

    logger.info("Transfer complete.");

    return result;
  }
}
