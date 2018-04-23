package primrose.rpcservices.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.ContactsRpc;
import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactEdit;
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
  public String create(ContactCreate contact) {
    validationSupport.validate("data", contact);

    return contactService.create(contact);
  }

  @Override
  public String update(String contactCode, ContactEdit contact) {
    validationSupport.validate("data", contact);

    contactService.update(contactCode, contact);

    return contactCode;
  }

  @Override
  public ListResult<ContactReducedDisplay> list(Pagination pagination) {
    validationSupport.validate("pagination", pagination);

    return contactService.list(pagination);
  }

  @Override
  public ContactFullDisplay get(String contactCode) {
    return contactService.get(contactCode);
  }

  @Override
  public String delete(String contactCode) {
    contactService.delete(contactCode);
    return contactCode;
  }

  @Override
  public Set<String> delete(Set<String> contactCodes) {
    contactService.delete(contactCodes);
    return contactCodes;
  }

}
