package primrose.rpcservices;

import java.util.Set;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/customers")
public interface Customers {

  CustomersSearchResult search(@JsonRpcParam("search") Search search);

  @JsonRpcErrors({
      @JsonRpcError(exception = IllegalArgumentException.class, code = -32600)
  })
  long create(@JsonRpcParam("customer") CustomerCreate customer);

  long delete(@JsonRpcParam("customer") long id);

  Set<Long> delete(@JsonRpcParam("customers") Set<Long> ids);
}
