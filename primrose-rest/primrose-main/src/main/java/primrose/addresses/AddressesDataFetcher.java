package primrose.addresses;

import java.util.List;
import java.util.stream.Collectors;

import graphql.execution.batched.Batched;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.graphql.GQLDataFetcher;
import primrose.model.BaseOutputAccount;
import primrose.model.BaseOutputAccountAddress;

@GQLDataFetcher(type = "Account", property = "addresses")
public class AddressesDataFetcher implements DataFetcher<List<List<BaseOutputAccountAddress>>> {

  private final AddressesService addressService;

  public AddressesDataFetcher(final AddressesService addressService) {
    this.addressService = addressService;
  }

  @Override
  @Batched
  public List<List<BaseOutputAccountAddress>> get(final DataFetchingEnvironment environment) {
    final List<BaseOutputAccount> sources = environment.getSource();
    return addressService.loadByAccountId(sources.stream().map(BaseOutputAccount::id).collect(Collectors.toList()));
  }

}
