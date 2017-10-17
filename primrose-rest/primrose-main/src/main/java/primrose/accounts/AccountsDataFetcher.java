package primrose.accounts;

import java.util.List;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.graphql.GQLDataFetcher;
import primrose.model.BaseOutputAccount;
import primrose.pagging.sort.SortUtil;

@GQLDataFetcher(type = "Query", property = "accounts")
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
