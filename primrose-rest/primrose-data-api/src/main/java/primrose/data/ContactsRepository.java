package primrose.data;

import java.util.List;

import primrose.model.input.BaseInputContact;
import primrose.model.output.BaseOutputAccountContact;

public interface ContactsRepository {

  int count();

  void insert(
    String contactId,
    BaseInputContact contact,
    String user);

  List<List<BaseOutputAccountContact>> loadByAccountId(List<String> accountId);

  String nextValContact();

  void update(
    String contactId,
    BaseInputContact contact,
    String user);

}