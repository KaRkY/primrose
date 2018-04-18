package primrose.rpcservices.impl;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.CustomersRpc;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerSearch;
import primrose.service.customer.CustomerService;
import primrose.spring.ValidationSupport;

@AutoJsonRpcServiceImpl
@Component
public class CustomersRpcImpl implements CustomersRpc {

  private CustomerService      customerService;
  private ValidationSupport validationSupport;

  public CustomersRpcImpl(CustomerService customerService, ValidationSupport validationSupport) {
    this.customerService = customerService;
    this.validationSupport = validationSupport;
  }

  @Override
  public SearchResult<CustomerSearch> search(Search search) {
    validationSupport.validate("search", search);

    return customerService.search(search);
  }

  @Override
  public long create(@Valid CustomerCreate customer) {
    validationSupport.validate("customer", customer);

    return customerService.create(customer);
  }

  @Override
  public Customer get(long customer) {
    return customerService.get(customer);
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
