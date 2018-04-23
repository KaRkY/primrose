package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;

@JsonRpcService("/customers")
public interface CustomersRpc {

  ListResult<CustomerReducedDisplay> list(@JsonRpcParam("pagination") Pagination pagination);

  String create(@JsonRpcParam("customer") CustomerCreate customer);

  CustomerFullDisplay get(@JsonRpcParam("customerCode") String customerCode);

  String deactivate(@JsonRpcParam("customerCode") String customerCode);

  Set<String> deactivate(@JsonRpcParam("customerCodes") Set<String> customerCodes);

  String edit(@JsonRpcParam("customerCode") String customerCode, @JsonRpcParam("customer") CustomerCreate customer);
}
