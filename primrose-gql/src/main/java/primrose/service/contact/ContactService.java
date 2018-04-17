package primrose.service.contact;

import primrose.service.Search;
import primrose.service.SearchResult;

public interface ContactService {

  SearchResult<ContactSearch> search(Search search);

  Contact get(long contactId);

  long create(ContactCreate contact);
}
