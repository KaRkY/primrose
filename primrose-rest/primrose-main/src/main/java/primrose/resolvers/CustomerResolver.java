package primrose.resolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

import graphql.execution.batched.Batched;
import primrose.types.Account;
import primrose.types.Customer;
import primrose.types.ImmutableAccount;
import primrose.types.ImmutableTypedAddress;
import primrose.types.TypedAddress;

@Component
public class CustomerResolver implements GraphQLResolver<Customer> {

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
    return customers
      .stream()
      .map(key -> {
        List<Account> accounts = new ArrayList<>();
        accounts.add(ImmutableAccount.builder()
          .id(1L)
          .name("test")
          .build());
        return accounts;
      })
      .collect(Collectors.toList());
  }
}
