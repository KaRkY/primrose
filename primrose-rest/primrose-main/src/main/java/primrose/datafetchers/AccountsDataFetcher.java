package primrose.datafetchers;

import java.util.List;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.model.output.BaseOutputAccount;
import primrose.model.sort.SortUtil;
import primrose.services.AccountsService;

public class AccountsDataFetcher implements DataFetcher<List<BaseOutputAccount>> {

  private final AccountsService accountsService;

  public AccountsDataFetcher(final AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @Override
  public List<BaseOutputAccount> get(final DataFetchingEnvironment environment) {
    return accountsService.load(
      environment.getArgument("page"),
      environment.getArgument("size"),
      SortUtil.parseSort(environment.getArgument("sort")));
  }
}
