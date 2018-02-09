package primrose.resolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

import graphql.execution.batched.Batched;
import primrose.types.Account;
import primrose.types.Customer;
import primrose.types.ImmutableCustomer;
import primrose.types.ImmutableTypedAddress;
import primrose.types.TypedAddress;

@Component
public class AccountResolver implements GraphQLResolver<Account> {

  @Batched
  public List<List<TypedAddress>> getAddresses(List<Account> accounts) {
    return accounts
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
  public List<Customer> getCustomer(List<Account> accounts) {
    return accounts
      .stream()
      .map(key -> ImmutableCustomer.builder()
        .id(1L)
        .type("test")
        .relationType("test")
        .fullName("sdpifh")
        .email("asdiof")
        .build())
      .collect(Collectors.toList());
  }
}
