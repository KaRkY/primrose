package primrose.dataimport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.accounts.Account;
import primrose.accounts.AccountsService;
import primrose.accounts.ImmutableAccount;
import primrose.addresses.Address;
import primrose.addresses.AddressesService;
import primrose.contacts.Contact;
import primrose.contacts.ContactsService;

@Service
public class DataImportService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final AccountsService accountsService;
  private final AddressesService addressService;
  private final ContactsService contactsService;

  public DataImportService(
    final AccountsService accountsService,
    final AddressesService addressService,
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
      final Account savedAccount = accountsService.save(account);
      final Map<String, List<Address>> addresses = new LinkedHashMap<>();
      final Map<String, List<Contact>> contacts = new LinkedHashMap<>();

      account.addresses().forEach((key, value) -> {
        value.forEach(address -> {
          final Address savedAddress = addressService.save(address);
          accountsService.addAddress(savedAccount.id(), savedAddress.id(), key);
          addresses.computeIfAbsent(key, type -> new ArrayList<>()).add(savedAddress);
        });
      });

      account.contacts().forEach((key, value) -> {
        value.forEach(contact -> {
          final Contact savedContact = contactsService.save(contact);
          accountsService.addContact(savedAccount.id(), savedContact.id(), key);
          contacts.computeIfAbsent(key, type -> new ArrayList<>()).add(savedContact);
        });
      });

      result.add(ImmutableAccount
        .builder()
        .from(savedAccount)
        .addresses(addresses)
        .contacts(contacts)
        .build());
      current++;

      if (current % 100 == 0) {
        logger.info("Transfering accounts {}% done", current / (double) size * 100);
      }
    }

    logger.info("Transfer complete.");

    return result;
  }
}
