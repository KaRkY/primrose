package primrose.rpcservices.impl;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.CustomersRpc;
import primrose.service.ListResult;
import primrose.service.Pagination;
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
  public ListResult<CustomerReducedDisplay> list(Pagination pagination) {
    validationSupport.validate("pagination", pagination);

    return customerService.list(pagination);
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
  public String deactivate(String customerCode) {
    System.out.println(customerCode);
    return customerCode;
  }

  @Override
  public Set<String> deactivate(Set<String> customerCodes) {
    System.out.println(customerCodes);
    return customerCodes;
  }

  @Override
  public String edit(String customerCode, CustomerCreate customer) {
    validationSupport.validate("data", customer);

    return customerService.edit(customerCode, customer);
  }

}
