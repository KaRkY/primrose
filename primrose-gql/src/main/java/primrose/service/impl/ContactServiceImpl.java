package primrose.service.impl;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

import primrose.data.ContactRepository;
import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.account.AccountCode;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCode;
import primrose.service.contact.ContactPreview;
import primrose.service.contact.ContactService;
import primrose.service.customer.CustomerCode;

@Component
public class ContactServiceImpl implements ContactService {

  private ContactRepository contactRepository;

  public ContactServiceImpl(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }

  @Override
  @Transactional
  // TODO implement meta validation
  public ContactCode create(Contact contact) {
    ContactCode contactCode = contactRepository.generate();
    contactRepository.create(contact
      .toBuilder()
      .code(contactCode)
      .emails(contact
        .getEmails()
        .stream()
        .map(email -> email.toBuilder().value(email.getValue().toLowerCase()).build())
        .collect(Collectors.toList()))
      .phoneNumbers(contact
        .getPhoneNumbers()
        .stream()
        .map(phoneNumber -> phoneNumber.toBuilder().value(phoneNumber.getValue().toLowerCase()).build())
        .collect(Collectors.toList()))
      .build());
    return contactCode;
  }

  @Override
  @Transactional
  // TODO implement meta validation
  public ContactCode update(Contact contact) {
    contactRepository.update(contact
      .toBuilder()
      .emails(contact
        .getEmails()
        .stream()
        .map(email -> email.toBuilder().value(email.getValue().toLowerCase()).build())
        .collect(Collectors.toList()))
      .phoneNumbers(contact
        .getPhoneNumbers()
        .stream()
        .map(phoneNumber -> phoneNumber.toBuilder().value(phoneNumber.getValue().toLowerCase()).build())
        .collect(Collectors.toList()))
      .build());
    return contact.getCode();
  }

  @Override
  public ListResult<ContactPreview> list(Pagination pagination) {
    ImmutableList<ContactPreview> data = contactRepository.list(pagination);
    long count = contactRepository.count(pagination);
    return ListResult.<ContactPreview>builder()
      .count(count)
      .data(data)
      .build();
  }

  @Override
  public ListResult<ContactPreview> list(AccountCode accountCode, Pagination pagination) {
    return null;
  }

  @Override
  public ListResult<ContactPreview> list(CustomerCode customerCode, Pagination pagination) {
    return null;
  }

  @Override
  public Contact get(ContactCode code) {
    return contactRepository.get(code);
  }

}
