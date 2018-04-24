package primrose.service.account;

import java.util.Set;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.ContactCode;
import primrose.service.customer.CustomerCode;

public interface AccountService {

  AccountCode create(CustomerCode customerCode, Account account);

  AccountCode update(Account account);

  ListResult<AccountPreview> list(CustomerCode customerCode, Pagination pagination);

  ListResult<AccountPreview> list(Pagination pagination);

  Account get(AccountCode accountCode);

  AccountCode close(AccountCode accountCode);

  AccountCode reopen(AccountCode accountCode);

  void move(AccountCode accountCode, CustomerCode toCustomer);

  void add(ContactCode contactCode);

  void add(Set<ContactCode> contactCodes);

  void remove(ContactCode contactCode);

  void remove(Set<ContactCode> contactCodes);
}
