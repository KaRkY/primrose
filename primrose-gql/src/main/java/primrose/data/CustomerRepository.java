package primrose.data;

import com.google.common.collect.ImmutableList;

import primrose.service.Pagination;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCode;
import primrose.service.customer.CustomerPreview;

public interface CustomerRepository {

  Customer get(CustomerCode code);

  long count(Pagination pagination);

  ImmutableList<CustomerPreview> list(Pagination pagination);

  CustomerCode update(Customer customer);

  CustomerCode create(Customer customer);

}
