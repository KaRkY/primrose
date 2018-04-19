package primrose.rpcservices.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.ContactsRpc;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;
import primrose.service.contact.ContactService;
import primrose.spring.ValidationSupport;

@AutoJsonRpcServiceImpl
@Component
public class ContactsRpcImpl implements ContactsRpc {

  private ContactService    contactService;
  private ValidationSupport validationSupport;

  public ContactsRpcImpl(ContactService contactService, ValidationSupport validationSupport) {
    this.contactService = contactService;
    this.validationSupport = validationSupport;
  }

  @Override
  public SearchResult<ContactReducedDisplay> search(Search search) {
    validationSupport.validate("search", search);

    return contactService.search(search);
  }

  @Override
  public String create(ContactCreate contact) {
    validationSupport.validate("contact", contact);

    return contactService.create(contact);
  }

  @Override
  public ContactFullDisplay get(String customerCode) {
    return contactService.get(customerCode);
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
  public String edit(String customerCode, ContactCreate contact) {
    validationSupport.validate("data", contact);

    return contactService.edit(customerCode, contact);
  }

}
