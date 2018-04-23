package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerEdit;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;

@JsonRpcService("/customers")
public interface CustomersRpc {

  String create(@JsonRpcParam("customer") CustomerCreate customer);

  String update(@JsonRpcParam("customerCode") String customerCode, @JsonRpcParam("customer") CustomerEdit customer);

  ListResult<CustomerReducedDisplay> list(@JsonRpcParam("pagination") Pagination pagination);

  CustomerFullDisplay get(@JsonRpcParam("customerCode") String customerCode);

  String delete(@JsonRpcParam("customerCode") String customerCode);

  Set<String> delete(@JsonRpcParam("customerCodes") Set<String> customerCodes);
}
