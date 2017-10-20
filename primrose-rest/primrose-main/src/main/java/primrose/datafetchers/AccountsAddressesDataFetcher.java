package primrose.datafetchers;

import java.util.List;
import java.util.stream.Collectors;

import graphql.execution.batched.Batched;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.model.output.BaseOutputAccount;
import primrose.model.output.BaseOutputAccountAddress;
import primrose.services.AddressesService;

public class AccountsAddressesDataFetcher implements DataFetcher<List<List<BaseOutputAccountAddress>>> {

  private final AddressesService addressService;

  public AccountsAddressesDataFetcher(final AddressesService addressService) {
    this.addressService = addressService;
  }

  @Override
  @Batched
  public List<List<BaseOutputAccountAddress>> get(final DataFetchingEnvironment environment) {
    final List<BaseOutputAccount> sources = environment.getSource();
    return addressService.loadByAccountId(sources.stream().map(BaseOutputAccount::id).collect(Collectors.toList()));
  }

}
