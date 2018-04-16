package primrose.rpcservices.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.data.CustomerRepository;
import primrose.error.ArgumentValidationException;
import primrose.rpcservices.CustomerCreate;
import primrose.rpcservices.Customers;
import primrose.rpcservices.CustomersSearchResult;
import primrose.rpcservices.Search;
import primrose.services.ImmutableCustomersSearchResult;

@AutoJsonRpcServiceImpl
@Component
public class CustomersImpl implements Customers {

  private CustomerRepository customerRepository;

//  public CustomersImpl(CustomerRepository customerRepository) {
//    this.customerRepository = customerRepository;
//  }

  @Override
  public CustomersSearchResult search(Search search) {
    System.out.println(search);
    return ImmutableCustomersSearchResult.builder()
        .count(0)
        .build();
  }

  @Override
  public long create(CustomerCreate customer) {
    System.out.println(customer);
    throw new ArgumentValidationException("fullName", "emails[0].type");
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
