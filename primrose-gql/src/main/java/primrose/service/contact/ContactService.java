package primrose.service.contact;

import primrose.service.ListResult;
import primrose.service.Pagination;

public interface ContactService {

  ListResult<ContactReducedDisplay> list(Pagination pagination);

  ContactFullDisplay get(String contactCode);

  String create(ContactCreate contact);

  String edit(String contactCode, ContactCreate contact);
}
