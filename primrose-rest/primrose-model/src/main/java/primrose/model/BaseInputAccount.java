package primrose.model;

import java.util.List;

public interface BaseInputAccount extends BaseAccount {

  List<BaseInputAccountAddress> addresses();

  List<BaseInputAccountContact> contacts();
}
