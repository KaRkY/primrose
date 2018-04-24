package primrose.rpcservices.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.CustomersRpc;
import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerFullDisplay;
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
  public String create(CustomerCreate customer) {
    validationSupport.validate("data", customer);

    return customerService.create(customer);
  }

  @Override
  public String update(String customerCode, Customer customer) {
    validationSupport.validate("data", customer);

    customerService.update(customerCode, customer);

    return customerCode;
  }

  @Override
  public ListResult<CustomerPreview> list(Pagination pagination) {
    validationSupport.validate("pagination", pagination);

    return customerService.list(pagination);
  }

  @Override
  public CustomerFullDisplay get(String customerCode) {
    return customerService.get(customerCode);
  }

  @Override
  public String delete(String customerCode) {
    customerService.delete(customerCode);
    return customerCode;
  }

  @Override
  public Set<String> delete(Set<String> customerCodes) {
    customerService.delete(customerCodes);
    return customerCodes;
  }

}
