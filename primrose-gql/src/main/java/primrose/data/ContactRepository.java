package primrose.data;

import java.util.List;

import primrose.service.Email;
import primrose.service.Phone;
import primrose.service.Search;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactSearch;

public interface ContactRepository {
  long create(ContactCreate contact);

  List<ContactSearch> search(Search search);

  long count(Search search);

  Contact get(long contactId, List<Email> emails, List<Phone> phones);
}
