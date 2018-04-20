package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;

@JsonRpcService("/customers")
public interface CustomersRpc {

   SearchResult<CustomerReducedDisplay> search(@JsonRpcParam("search") Search search);

   String create(@JsonRpcParam("customer") CustomerCreate customer);

   CustomerFullDisplay get(@JsonRpcParam("customerCode") String customerCode);

   String delete(@JsonRpcParam("customerCode") String customerCode);

   Set<String> delete(@JsonRpcParam("customerCodes") Set<String> customerCodes);

   String edit(@JsonRpcParam("customerCode") String customerCode, @JsonRpcParam("customer") CustomerCreate customer);
}
