package primrose.rpcservices.impl;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.CustomersRpc;
import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCode;
import primrose.service.customer.CustomerPreview;
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
  public CustomerCode create(Customer customer) {
    validationSupport.validate("data", customer);

    return customerService.create(customer);
  }

  @Override
  public CustomerCode update(Customer customer) {
    validationSupport.validate("data", customer);

    return customerService.update(customer);
  }

  @Override
  public ListResult<CustomerPreview> list(Pagination pagination) {
    validationSupport.validate("pagination", pagination);

    return customerService.list(pagination);
  }

  @Override
  public Customer get(CustomerCode customerCode) {
    return customerService.get(customerCode);
  }

}
