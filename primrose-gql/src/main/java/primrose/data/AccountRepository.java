package primrose.data;

import java.util.List;

import primrose.service.CodeId;
import primrose.service.EmailFullDisplay;
import primrose.service.Pagination;
import primrose.service.PhoneNumber;
import primrose.service.account.Account;
import primrose.service.account.AccountFullDisplay;
import primrose.service.account.AccountPreview;

public interface AccountRepository {

  CodeId generateCode(CodeId customerCode);

  CodeId codeId(CodeId customerCode, String accountCode);

  void create(CodeId customerCode, CodeId accountCode, Account account);

  List<AccountPreview> list(CodeId customerCode, Pagination search);

  long count(CodeId customerCode, Pagination search);

  AccountFullDisplay get(CodeId customerCode, CodeId accountCode, List<EmailFullDisplay> emails,
    List<PhoneNumber> phones);

  AccountFullDisplay getForUpdate(CodeId customerCode, CodeId accountCode, List<EmailFullDisplay> emails,
    List<PhoneNumber> phones);

  void deactivate(CodeId customerCode, CodeId accountCode);
}
