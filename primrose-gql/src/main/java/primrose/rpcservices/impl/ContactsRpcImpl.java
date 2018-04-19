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
  public long create(ContactCreate contact) {
    validationSupport.validate("contact", contact);

    return contactService.create(contact);
  }

  @Override
  public ContactFullDisplay get(long contactId) {
    return contactService.get(contactId);
  }

  @Override
  public long delete(long contactId) {
    System.out.println(contactId);
    return 0;
  }

  @Override
  public Set<Long> delete(Set<Long> contactIds) {
    System.out.println(contactIds);
    return null;
  }

  @Override
  public long edit(long contactId, ContactCreate contact) {
    validationSupport.validate("data", contact);

    return contactService.edit(contactId, contact);
  }

}
