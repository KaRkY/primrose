package primrose.rpcservices.impl;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.ContactsRpc;
import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCode;
import primrose.service.contact.ContactPreview;
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
  public ContactCode create(Contact contact) {
    validationSupport.validate("data", contact);

    return contactService.create(contact);
  }

  @Override
  public ContactCode update(Contact contact) {
    validationSupport.validate("data", contact);

    return contactService.update(contact);
  }

  @Override
  public ListResult<ContactPreview> list(Pagination pagination) {
    validationSupport.validate("pagination", pagination);

    return contactService.list(pagination);
  }

  @Override
  public Contact get(ContactCode contactCode) {
    return contactService.get(contactCode);
  }

}
