package primrose.data;

import primrose.service.customer.CustomerCreate;

public interface CustomerRepository {

  long create(CustomerCreate customer);
}
