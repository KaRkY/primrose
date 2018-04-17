package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerSearch;

@JsonRpcService("/customers")
public interface CustomersRpc {

  SearchResult<CustomerSearch> search(@JsonRpcParam("search") Search search);

  long create(@JsonRpcParam("customer") CustomerCreate customer);

  Customer get(@JsonRpcParam("customer") long customer);

  long delete(@JsonRpcParam("customer") long id);

  Set<Long> delete(@JsonRpcParam("customers") Set<Long> ids);
}
