package primrose.services.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.services.CustomerCreate;
import primrose.services.Customers;
import primrose.services.CustomersSearchResult;
import primrose.services.Search;

@AutoJsonRpcServiceImpl
@Component
public class CustomersImpl implements Customers {

  @Override
  public CustomersSearchResult search(Search search) {
    System.out.println(search);
    return null;
  }

  @Override
  public long create(CustomerCreate customer) {
    return 0;
  }

  @Override
  public long delete(long id) {
    System.out.println(id);
    return 0;
  }

  @Override
  public Set<Long> delete(Set<Long> ids) {
    System.out.println(ids);
    return null;
  }

}
