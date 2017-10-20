package primrose.datafetchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.model.input.BaseInputAccount;
import primrose.model.output.BaseOutputAccount;
import primrose.services.AccountsService;

public class ImportAccountsDataFetcher implements DataFetcher<List<BaseOutputAccount>> {

  private final AccountsService accountsService;
  private final ObjectMapper    objectMapper;

  public ImportAccountsDataFetcher(
    final AccountsService accountsService,
    final ObjectMapper objectMapper) {
    this.accountsService = accountsService;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<BaseOutputAccount> get(final DataFetchingEnvironment environment) {
    final List<Map<String, Object>> accounts = environment.getArgument("accounts");
    final List<BaseOutputAccount> result = new ArrayList<>();

    final List<BaseInputAccount> convertedAccounts = objectMapper
      .convertValue(accounts, new TypeReference<List<BaseInputAccount>>() {
      });

    for (final BaseInputAccount account : convertedAccounts) {

      final String accountId = accountsService.create(account);

      result.add(accountsService.loadById(accountId));
    }
    return result;
  }
}
