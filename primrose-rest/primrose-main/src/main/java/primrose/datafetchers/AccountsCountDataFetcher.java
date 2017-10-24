package primrose.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.services.AccountsService;

public class AccountsCountDataFetcher implements DataFetcher<Integer> {

  private final AccountsService accountsService;

  public AccountsCountDataFetcher(final AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @Override
  public Integer get(final DataFetchingEnvironment environment) {
    return accountsService.count();
  }
}
