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
import primrose.addresses.AddressesService;
import primrose.contacts.ContactsService;

@Service
public class DataImportService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final AccountsService accountsService;
  private final AddressesService addressService;
  private final ContactsService contactsService;

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
      final Account savedAccount = accountsService.save(account);
      final Builder accountBuilder = ImmutableAccount.builder().from(savedAccount);

      account.addresses().forEach((key, value) -> {
        accountBuilder.putAddresses(key, addressService.save(savedAccount.code(), key, value));
      });

      account.contacts().forEach((key, value) -> {
        accountBuilder.putContacts(key, contactsService.save(savedAccount.code(), key, value));
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
