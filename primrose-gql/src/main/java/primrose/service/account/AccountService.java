package primrose.service.account;

import primrose.service.ListResult;
import primrose.service.Pagination;

public interface AccountService {

  ListResult<AccountReducedDisplay> list(String contactCode, Pagination pagination);

  AccountFullDisplay get(String contactCode, String accountCode);

  String create(String contactCode, AccountCreate account);

  String edit(String contactCode, String accountCode, AccountCreate account);
}
