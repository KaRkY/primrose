package primrose.rpcservices.customer;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerSearch;

@JsonRpcService("/customers")
public interface Customers {

  SearchResult<CustomerSearch> search(@JsonRpcParam("search") Search search);

  long create(@JsonRpcParam("customer") CustomerCreate customer);

  long delete(@JsonRpcParam("customer") long id);

  Set<Long> delete(@JsonRpcParam("customers") Set<Long> ids);
}
