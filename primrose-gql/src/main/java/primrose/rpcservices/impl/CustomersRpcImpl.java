package primrose.rpcservices.impl;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.CustomersRpc;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;
import primrose.service.customer.CustomerService;
import primrose.spring.ValidationSupport;

@AutoJsonRpcServiceImpl
@Component
public class CustomersRpcImpl implements CustomersRpc {

  private CustomerService   customerService;
  private ValidationSupport validationSupport;

  public CustomersRpcImpl(CustomerService customerService, ValidationSupport validationSupport) {
    this.customerService = customerService;
    this.validationSupport = validationSupport;
  }

  @Override
  public SearchResult<CustomerReducedDisplay> search(Search search) {
    validationSupport.validate("search", search);

    return customerService.search(search);
  }

  @Override
  public String create(@Valid CustomerCreate customer) {
    validationSupport.validate("customer", customer);

    return customerService.create(customer);
  }

  @Override
  public CustomerFullDisplay get(String customerCode) {
    return customerService.get(customerCode);
  }

  @Override
  public String delete(String customerCode) {
    System.out.println(customerCode);
    return "";
  }

  @Override
  public Set<String> delete(Set<String> customerCodes) {
    System.out.println(customerCodes);
    return null;
  }

  @Override
  public String edit(String customerCode, CustomerCreate customer) {
    validationSupport.validate("data", customer);

    return customerService.edit(customerCode, customer);
  }

}
