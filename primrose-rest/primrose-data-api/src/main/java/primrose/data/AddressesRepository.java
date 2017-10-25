package primrose.data;

import java.util.List;

import primrose.model.input.BaseInputAddress;
import primrose.model.output.BaseOutputAccountAddress;

public interface AddressesRepository {

  int count();

  void insert(String addressId, BaseInputAddress address);

  List<List<BaseOutputAccountAddress>> loadByAccountId(List<String> accountId);

  String nextValAddresses();

  void update(String addressId, BaseInputAddress address);

}
