package primrose.data;

import java.util.List;
import java.util.Optional;

import primrose.model.input.BaseInputAccount;
import primrose.model.output.BaseOutputAccount;
import primrose.model.output.BaseOutputAccountAddressType;
import primrose.model.output.BaseOutputAccountContactType;
import primrose.model.output.BaseOutputAccountType;
import primrose.model.sort.Sort;

public interface AccountsRepository {

  void assignAddress(
    String accountId,
    String addressId,
    String addressType,
    String user);

  void assignContact(
    String accountId,
    String contactId,
    String contactType,
    String user);

  int count();

  void insert(String accountId, BaseInputAccount account, String user);

  List<BaseOutputAccount> load(Integer page, Integer size, Sort sort);

  Optional<BaseOutputAccountAddressType> loadAddressType(String type);

  List<BaseOutputAccountAddressType> loadAddressTypes();

  Optional<BaseOutputAccount> loadById(String accountId);

  Optional<BaseOutputAccount> loadByName(String accountName);

  Optional<BaseOutputAccountContactType> loadContactType(String type);

  List<BaseOutputAccountContactType> loadContactTypes();

  Optional<BaseOutputAccountType> loadType(String type);

  List<BaseOutputAccountType> loadTypes();

  String nextValAccounts();

  boolean typeExists(String type);

  void update(String accountId, BaseInputAccount account, String user);

}
