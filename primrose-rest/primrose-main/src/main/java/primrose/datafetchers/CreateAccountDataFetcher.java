package primrose.datafetchers;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.model.input.BaseInputAccount;
import primrose.model.output.BaseOutputAccount;
import primrose.services.AccountsService;

public class CreateAccountDataFetcher implements DataFetcher<BaseOutputAccount> {

  private final AccountsService accountsService;
  private final ObjectMapper    objectMapper;

  public CreateAccountDataFetcher(
    final AccountsService accountsService,
    final ObjectMapper objectMapper) {
    this.accountsService = accountsService;
    this.objectMapper = objectMapper;
  }

  @Override
  public BaseOutputAccount get(final DataFetchingEnvironment environment) {
    final Map<String, Object> account = environment.getArgument("account");

    final BaseInputAccount convertedAccount = objectMapper
      .convertValue(account, BaseInputAccount.class);

    final String accountId = accountsService.create(convertedAccount);

    return accountsService.loadById(accountId);
  }
}
