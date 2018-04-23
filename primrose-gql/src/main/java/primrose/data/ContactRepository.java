package primrose.data;

import java.util.Set;

import com.google.common.collect.ImmutableList;

import primrose.service.Pagination;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactEdit;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

public interface ContactRepository {

  String create(ContactCreate contact);

  void update(String code, ContactEdit contact);

  ImmutableList<ContactReducedDisplay> list(Pagination pagination);

  long count(Pagination pagination);

  ContactFullDisplay get(String code);

  void delete(String code);

  void delete(Set<String> codes);

}
