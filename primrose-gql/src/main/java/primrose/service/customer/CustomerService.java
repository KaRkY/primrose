package primrose.service.customer;

import java.util.Set;

import primrose.service.ListResult;
import primrose.service.Pagination;

public interface CustomerService {

  String create(CustomerCreate customer);

  void update(String code, CustomerEdit customer);

  ListResult<CustomerReducedDisplay> list(Pagination pagination);

  CustomerFullDisplay get(String code);

  void delete(String code);

  void delete(Set<String> codes);

}
