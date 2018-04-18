package primrose.rpcservices.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.ContactsRpc;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactSearch;
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
  public SearchResult<ContactSearch> search(Search search) {
    validationSupport.validate("search", search);

    return contactService.search(search);
  }

  @Override
  public long create(ContactCreate contact) {
    validationSupport.validate("contact", contact);

    return contactService.create(contact);
  }

  @Override
  public Contact get(long contact) {
    return contactService.get(contact);
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
