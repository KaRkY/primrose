package primrose.data;

import java.util.List;

import primrose.service.Email;
import primrose.service.Phone;
import primrose.service.Search;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerSearch;

public interface CustomerRepository {

  long create(CustomerCreate customer);

  List<CustomerSearch> search(Search search);

  long count(Search search);

  Customer get(long customerId, List<Email> emails, List<Phone> phones);
}
