package primrose.rpcservices.impl;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.error.ArgumentValidationException;
import primrose.rpcservices.ContactsRpc;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactSearch;
import primrose.service.contact.ContactService;

@AutoJsonRpcServiceImpl
@Component
public class ContactsRpcImpl implements ContactsRpc {

  private ContactService       contactService;
  private Validator            validator;
  private MessageCodesResolver messageCodesResolver;

  public ContactsRpcImpl(ContactService contactService, Validator validator, MessageCodesResolver messageCodesResolver) {
    this.contactService = contactService;
    this.validator = validator;
    this.messageCodesResolver = messageCodesResolver;
  }

  @Override
  public SearchResult<ContactSearch> search(Search search) {
    DirectFieldBindingResult bindingResult = new DirectFieldBindingResult(search, "search");
    bindingResult.setMessageCodesResolver(messageCodesResolver);
    validator.validate(search, bindingResult);
    if (bindingResult.hasErrors()) { throw new ArgumentValidationException(bindingResult); }

    return contactService.search(search);
  }

  @Override
  public long create(ContactCreate contact) {
    DirectFieldBindingResult bindingResult = new DirectFieldBindingResult(contact, "contact");
    bindingResult.setMessageCodesResolver(messageCodesResolver);
    validator.validate(contact, bindingResult);
    if (bindingResult.hasErrors()) { throw new ArgumentValidationException(bindingResult); }
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
