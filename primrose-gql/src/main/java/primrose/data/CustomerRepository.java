package primrose.data;

import primrose.rpcservices.CustomerCreate;

public interface CustomerRepository {

  long create(CustomerCreate customer);
}
