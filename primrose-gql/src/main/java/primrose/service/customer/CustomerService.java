package primrose.service.customer;

import primrose.service.Search;
import primrose.service.SearchResult;

public interface CustomerService {

  SearchResult<CustomerReducedDisplay> search(Search search);

  CustomerFullDisplay get(String customerCode);

  String create(CustomerCreate customer);

  String edit(String customerCode, CustomerCreate customer);
}
