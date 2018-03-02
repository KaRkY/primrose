package primrose.resolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

import graphql.execution.batched.Batched;
import primrose.types.output.Account;
import primrose.types.output.Customer;
import primrose.types.output.ImmutableCustomer;
import primrose.types.output.ImmutableTypedAddress;
import primrose.types.output.TypedAddress;

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
