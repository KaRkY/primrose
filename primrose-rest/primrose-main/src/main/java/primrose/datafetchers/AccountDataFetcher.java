package primrose.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.model.output.BaseOutputAccount;
import primrose.services.AccountsService;

public class AccountDataFetcher implements DataFetcher<BaseOutputAccount> {
  private final AccountsService accountsService;

  public AccountDataFetcher(final AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @Override
  public BaseOutputAccount get(final DataFetchingEnvironment environment) {
    return accountsService.loadById(environment.getArgument("accountId"));
  }

}
