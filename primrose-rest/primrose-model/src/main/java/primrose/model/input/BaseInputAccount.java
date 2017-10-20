package primrose.model.input;

import java.util.List;

import primrose.model.BaseAccount;

public interface BaseInputAccount extends BaseAccount {

  List<BaseInputAccountAddress> addresses();

  List<BaseInputAccountContact> contacts();
}
