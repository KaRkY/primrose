package primrose.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.schema.DataFetcher;
import primrose.datafetchers.AccountDataFetcher;
import primrose.datafetchers.AccountsAddressesDataFetcher;
import primrose.datafetchers.AccountsContactsDataFetcher;
import primrose.datafetchers.AccountsCountDataFetcher;
import primrose.datafetchers.AccountsDataFetcher;
import primrose.datafetchers.CreateAccountDataFetcher;
import primrose.datafetchers.ImportAccountsDataFetcher;
import primrose.model.output.BaseOutputAccount;
import primrose.model.output.BaseOutputAccountAddress;
import primrose.model.output.BaseOutputAccountContact;
import primrose.services.AccountsService;
import primrose.services.AddressesService;
import primrose.services.ContactsService;

@Configuration
public class GQLDataFetchers {

  private final AccountsService  accountsService;
  private final AddressesService addressesService;
  private final ContactsService  contactsService;
  private final ObjectMapper     objectMapper;

  public GQLDataFetchers(
    final AccountsService accountsService,
    final AddressesService addressesService,
    final ContactsService contactsService,
    final ObjectMapper objectMapper) {
    this.accountsService = accountsService;
    this.addressesService = addressesService;
    this.contactsService = contactsService;
    this.objectMapper = objectMapper;
  }

  public DataFetcher<List<List<BaseOutputAccountAddress>>> accountsAddresses() {
    return new AccountsAddressesDataFetcher(addressesService);
  }

  public DataFetcher<List<List<BaseOutputAccountContact>>> accountsContacts() {
    return new AccountsContactsDataFetcher(contactsService);
  }

  public DataFetcher<Integer> accountsCount() {
    return new AccountsCountDataFetcher(accountsService);
  }

  public DataFetcher<BaseOutputAccount> mutationCreateAccount() {
    return new CreateAccountDataFetcher(accountsService, objectMapper);
  }

  public DataFetcher<List<BaseOutputAccount>> mutationImportAccounts() {
    return new ImportAccountsDataFetcher(accountsService, objectMapper);
  }

  public DataFetcher<BaseOutputAccount> queryAccount() {
    return new AccountDataFetcher(accountsService);
  }

  public DataFetcher<List<BaseOutputAccount>> queryAccounts() {
    return new AccountsDataFetcher(accountsService);
  }
}
