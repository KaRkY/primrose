package primrose.service.contact;

import primrose.service.Search;
import primrose.service.SearchResult;

public interface ContactService {

  SearchResult<ContactReducedDisplay> search(Search search);

  ContactFullDisplay get(long contactId);

  long create(ContactCreate contact);

  long edit(long contactId, ContactCreate contact);
}
