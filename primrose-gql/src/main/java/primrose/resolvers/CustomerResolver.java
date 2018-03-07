package primrose.resolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

import graphql.execution.batched.Batched;
import primrose.query.AccountQuery;
import primrose.types.output.Account;
import primrose.types.output.Customer;
import primrose.types.output.ImmutableTypedAddress;
import primrose.types.output.TypedAddress;

@Component
public class CustomerResolver implements GraphQLResolver<Customer> {

  private AccountQuery accountQuery;

  public CustomerResolver(AccountQuery accountQuery) {
    this.accountQuery = accountQuery;
  }

  @Batched
  public List<List<TypedAddress>> getAddresses(List<Customer> customers) {
    return customers
      .stream()
      .map(key -> {
        List<TypedAddress> addresses = new ArrayList<>();
        addresses.add(ImmutableTypedAddress.builder()
          .type("test")
          .build());
        return addresses;
      })
      .collect(Collectors.toList());
  }

  @Batched
  public List<List<Account>> getAccounts(List<Customer> customers) {
    return accountQuery.list(customers.stream().map(Customer::id).collect(Collectors.toList()));
  }
}
