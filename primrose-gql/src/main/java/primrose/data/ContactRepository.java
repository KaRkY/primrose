package primrose.data;

import com.google.common.collect.ImmutableList;

import primrose.service.CodeId;
import primrose.service.Pagination;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

public interface ContactRepository {

  CodeId generateCode();

  CodeId codeId(String code);

  void create(CodeId code, ContactCreate contact);

  ImmutableList<ContactReducedDisplay> list(Pagination search);

  long count(Pagination search);

  ContactFullDisplay get(CodeId code);

  ContactFullDisplay getForUpdate(CodeId code);

  void deactivate(CodeId code);
}
