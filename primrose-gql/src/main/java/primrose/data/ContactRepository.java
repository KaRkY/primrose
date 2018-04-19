package primrose.data;

import java.util.List;

import primrose.service.EmailFullDisplay;
import primrose.service.PhoneFullDisplay;
import primrose.service.Search;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

public interface ContactRepository {
  long create(ContactCreate contact);

  List<ContactReducedDisplay> search(Search search);

  long count(Search search);

  ContactFullDisplay get(long contactId, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones);

  ContactFullDisplay getForUpdate(long contactId, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones);

  void deactivate(long contactId);
}
