package primrose.data;

import java.util.List;

import primrose.service.CodeId;
import primrose.service.EmailFullDisplay;
import primrose.service.Pagination;
import primrose.service.PhoneFullDisplay;
import primrose.service.account.AccountCreate;
import primrose.service.account.AccountFullDisplay;
import primrose.service.account.AccountReducedDisplay;

public interface AccountRepository {

  CodeId generateCode(CodeId customerCode);

  CodeId codeId(CodeId customerCode, String accountCode);

  void create(CodeId customerCode, CodeId accountCode, AccountCreate account);

  List<AccountReducedDisplay> list(CodeId customerCode, Pagination search);

  long count(CodeId customerCode, Pagination search);

  AccountFullDisplay get(CodeId customerCode, CodeId accountCode, List<EmailFullDisplay> emails,
      List<PhoneFullDisplay> phones);

  AccountFullDisplay getForUpdate(CodeId customerCode, CodeId accountCode, List<EmailFullDisplay> emails,
      List<PhoneFullDisplay> phones);

  void deactivate(CodeId customerCode, CodeId accountCode);
}
