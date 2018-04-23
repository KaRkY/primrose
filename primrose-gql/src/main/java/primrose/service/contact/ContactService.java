package primrose.service.contact;

import java.util.Set;

import primrose.service.ListResult;
import primrose.service.Pagination;

public interface ContactService {

  String create(ContactCreate customer);

  void update(String code, ContactEdit contact);

  ListResult<ContactReducedDisplay> list(Pagination pagination);

  ContactFullDisplay get(String code);

  void delete(String code);

  void delete(Set<String> codes);

}
