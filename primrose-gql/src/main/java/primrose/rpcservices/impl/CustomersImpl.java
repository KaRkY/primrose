package primrose.rpcservices.impl;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.error.ArgumentValidationException;
import primrose.rpcservices.customer.Customers;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerSearch;
import primrose.service.customer.CustomerService;

@AutoJsonRpcServiceImpl
@Component
public class CustomersImpl implements Customers {

  private CustomerService      customerService;
  private Validator            validator;
  private MessageCodesResolver messageCodesResolver;

  public CustomersImpl(CustomerService customerService, Validator validator, MessageCodesResolver messageCodesResolver) {
    this.customerService = customerService;
    this.validator = validator;
    this.messageCodesResolver = messageCodesResolver;
  }

  @Override
  public SearchResult<CustomerSearch> search(Search search) {
    DirectFieldBindingResult bindingResult = new DirectFieldBindingResult(search, "search");
    bindingResult.setMessageCodesResolver(messageCodesResolver);
    validator.validate(search, bindingResult);
    if (bindingResult.hasErrors()) { throw new ArgumentValidationException(bindingResult); }

    return customerService.search(search);
  }

  @Override
  public long create(CustomerCreate customer) {
    DirectFieldBindingResult bindingResult = new DirectFieldBindingResult(customer, "customer");
    bindingResult.setMessageCodesResolver(messageCodesResolver);
    validator.validate(customer, bindingResult);
    if (bindingResult.hasErrors()) { throw new ArgumentValidationException(bindingResult); }
    return customerService.create(customer);
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
