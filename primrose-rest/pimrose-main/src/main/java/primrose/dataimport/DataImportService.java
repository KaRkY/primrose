package primrose.dataimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.accounts.Account;
import primrose.accounts.AccountsService;
import primrose.addresses.Address;
import primrose.addresses.AddressesService;
import primrose.contacts.Contact;
import primrose.contacts.ContactsService;

@Service
public class DataImportService {
  private final Logger           logger = LoggerFactory.getLogger(getClass());
  private final AccountsService  accountsService;
  private final AddressesService addressService;
  private final ContactsService  contactsService;

  public DataImportService(final AccountsService accountsService, final AddressesService addressService,
    final ContactsService contactsService) {
    this.accountsService = accountsService;
    this.addressService = addressService;
    this.contactsService = contactsService;
  }

  @Transactional
  public List<Account> importAccounts(final List<Account> accounts) {
    final List<Account> result = new ArrayList<>();
    final int size = accounts.size();
    int current = 0;

    for (final Account account : accounts) {
      final Account importAccount = accountsService.save(account);

      if (account.getAddresses() != null) {
        importAccount.setAddresses(new HashMap<>());
        for (final Map.Entry<String, List<Address>> entry : account.getAddresses().entrySet()) {
          for (final Address address : entry.getValue()) {
            importAccount
              .getAddresses()
              .computeIfAbsent(
                entry.getKey(),
                key -> new ArrayList<>())
              .add(addressService.save(account.getCode(), entry.getKey(), address));
          }
        }

        importAccount.setContacts(new HashMap<>());
        for (final Map.Entry<String, List<Contact>> entry : account.getContacts().entrySet()) {
          for (final Contact contact : entry.getValue()) {
            importAccount
              .getContacts()
              .computeIfAbsent(entry.getKey(), key -> new ArrayList<>())
              .add(contactsService.save(account.getCode(), entry.getKey(), contact));
          }
        }
      }

      result.add(importAccount);
      current++;

      if (current % 100 == 0) {
        logger.info("Transfering accounts {}% done", current / (double) size * 100);
      }
    }

    logger.info("Transfer complete.");

    return result;
  }
}
