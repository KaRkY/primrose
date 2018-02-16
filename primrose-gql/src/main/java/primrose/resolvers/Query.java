package primrose.resolvers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import primrose.pagination.ImmutablePageable;
import primrose.pagination.SortDirection;
import primrose.query.CustomerQuery;
import primrose.types.Account;
import primrose.types.Customer;
import primrose.types.Pageable;
import primrose.types.PropertySort;

@Component
public class Query implements GraphQLQueryResolver {

  private CustomerQuery customerQuery;

  public Query(CustomerQuery customerQuery) {
    this.customerQuery = customerQuery;
  }

  public List<Customer> customers(Pageable pageable, List<PropertySort> propertySort) {
    return customerQuery.list(ImmutablePageable.builder()
      .pageNumber(pageable != null ? pageable.pageNumber() : 0)
      .pageSize(pageable != null ? pageable.pageSize() : 0)
      .sortProperties(propertySort != null ? Optional.of(propertySort.stream()
        .collect(Collectors.toMap(prop -> prop.propertyName(), prop -> prop.direction().orElse(SortDirection.DEFAULT)))) : Optional.empty())
      .build());
  }

  public Integer customersCount() {
    return customerQuery.count();
  }

  public Customer customer(Long id) {
    return customerQuery.load(id);
  }

  public List<Account> accounts(Long customer, Pageable pageable, List<PropertySort> propertySort) {

    return null;
  }

  public Integer accountsCount(Long customer) {
    return 0;
  }
}