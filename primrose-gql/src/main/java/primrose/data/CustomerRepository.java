package primrose.data;

import java.util.Set;

import com.google.common.collect.ImmutableList;

import primrose.service.Pagination;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerEdit;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;

public interface CustomerRepository {

  String create(CustomerCreate customer);

  void update(String code, CustomerEdit customer);

  ImmutableList<CustomerReducedDisplay> list(Pagination pagination);

  long count(Pagination pagination);

  CustomerFullDisplay get(String code);

  void delete(String code);

  void delete(Set<String> codes);

}
