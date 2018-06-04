package primrose.rpcservices;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCode;
import primrose.service.customer.CustomerPreview;

@JsonRpcService("/customers")
public interface CustomersRpc {

  CustomerCode create(@JsonRpcParam("customer") Customer customer);

  Customer read(@JsonRpcParam("customerCode") CustomerCode customerCode);

  CustomerCode update(@JsonRpcParam("customer") Customer customer);

  ListResult<CustomerPreview> list(@JsonRpcParam("pagination") Pagination pagination);

}
