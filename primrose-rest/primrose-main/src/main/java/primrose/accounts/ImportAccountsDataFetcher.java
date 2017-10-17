package primrose.accounts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.addresses.ImmutableInputAccountAddress;
import primrose.contacts.ImmutableInputAccountContact;
import primrose.graphql.GQLDataFetcher;
import primrose.model.BaseOutputAccount;

@GQLDataFetcher(type = "Mutation", property = "importAccounts")
public class ImportAccountsDataFetcher implements DataFetcher<List<BaseOutputAccount>> {

  private final AccountsService accountsService;

  public ImportAccountsDataFetcher(final AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @Override
  public List<BaseOutputAccount> get(final DataFetchingEnvironment environment) {
    final List<Map<String, Object>> accounts = environment.getArgument("accounts");
    final List<BaseOutputAccount> result = new ArrayList<>();

    for (final Map<String, Object> account : accounts) {
      final ImmutableInputAccount.Builder convertedAccount = ImmutableInputAccount.builder()
        .type(get(account, "type"))
        .displayName(get(account, "displayName"))
        .name(get(account, "name"))
        .email(get(account, "email"))
        .phone(get(account, "phone"))
        .website(get(account, "website"))
        .description(get(account, "description"));

      final List<Map<String, Object>> inputAddresses = get(account, "addresses");
      if (inputAddresses != null) {
        convertedAccount.addAllAddresses(inputAddresses
          .stream()
          .map(address -> ImmutableInputAccountAddress.builder()
            .type(get(address, "type"))
            .street(get(address, "street"))
            .streetNumber(get(address, "streetNumber"))
            .city(get(address, "city"))
            .postalCode(get(address, "postalCode"))
            .state(get(address, "state"))
            .country(get(address, "country"))
            .build())
          .collect(Collectors.toList()));
      }

      final List<Map<String, Object>> inputContacts = get(account, "contacts");
      if (inputContacts != null) {
        convertedAccount.addAllContacts(inputContacts
          .stream()
          .map(contact -> ImmutableInputAccountContact.builder()
            .type(get(contact, "type"))
            .name(get(contact, "name"))
            .email(get(contact, "email"))
            .phone(get(contact, "phone"))
            .build())
          .collect(Collectors.toList()));
      }

      final String accountId = accountsService.create(convertedAccount.build());

      result.add(accountsService.loadById(accountId));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private <T> T get(final Map<String, Object> map, final String property) {
    return (T) map.get(property);
  }
}
