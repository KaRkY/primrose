package primrose.data;

import com.google.common.collect.ImmutableList;

import primrose.service.Pagination;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCode;
import primrose.service.contact.ContactPreview;

public interface ContactRepository {

  Contact get(ContactCode code);

  long count(Pagination pagination);

  ImmutableList<ContactPreview> list(Pagination pagination);

  ContactCode update(Contact contact);

  ContactCode create(Contact contact);

}
