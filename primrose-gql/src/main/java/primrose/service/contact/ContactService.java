package primrose.service.contact;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.account.AccountCode;
import primrose.service.customer.CustomerCode;

public interface ContactService {

  ContactCode create(Contact contact);

  ContactCode update(Contact contact);

  ListResult<ContactPreview> list(Pagination pagination);

  ListResult<ContactPreview> list(CustomerCode customerCode, Pagination pagination);

  ListResult<ContactPreview> list(AccountCode accountCode, Pagination pagination);

  Contact get(ContactCode contactCode);

}
