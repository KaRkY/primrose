package primrose.service.customer;

import primrose.service.ListResult;
import primrose.service.Pagination;

public interface CustomerService {

  ListResult<CustomerReducedDisplay> list(Pagination search);

  CustomerFullDisplay get(String customerCode);

  String create(CustomerCreate customer);

  String edit(String customerCode, CustomerCreate customer);
}
