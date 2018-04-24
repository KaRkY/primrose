package primrose.service.impl;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

import primrose.data.ContactRepository;
import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactPreview;
import primrose.service.contact.ContactService;

@Component
public class ContactServiceImpl implements ContactService {

  private ContactRepository contactRepository;

  public ContactServiceImpl(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }

  @Override
  @Transactional
  // TODO implement meta validation
  public String create(ContactCreate contact) {
    return contactRepository.create(contact);
  }

  @Override
  @Transactional
  // TODO implement meta validation
  public void update(String code, Contact contact) {
    contactRepository.update(code, contact);
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
  public ContactFullDisplay get(String code) {
    return contactRepository.get(code);
  }

  @Override
  public void delete(String code) {
    contactRepository.delete(code);
  }

  @Override
  public void delete(Set<String> codes) {
    contactRepository.delete(codes);
  }

}
