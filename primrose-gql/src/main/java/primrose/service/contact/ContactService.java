package primrose.service.contact;

import primrose.service.Search;
import primrose.service.SearchResult;

public interface ContactService {

   SearchResult<ContactReducedDisplay> search(Search search);

   ContactFullDisplay get(String contactCode);

   String create(ContactCreate contact);

   String edit(String contactCode, ContactCreate contact);
}
