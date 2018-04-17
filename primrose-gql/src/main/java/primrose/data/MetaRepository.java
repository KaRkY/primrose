package primrose.data;

import java.util.List;

import primrose.service.MetaType;

public interface MetaRepository {

  List<MetaType> customer();

  boolean customer(String customer);

  List<MetaType> customerRelation();

  boolean customerRelation(String customerRelation);

  List<MetaType> address();

  boolean address(String address);

  List<MetaType> phoneNumber();

  boolean phoneNumber(String phoneNumber);

  List<MetaType> email();

  boolean email(String email);

  List<MetaType> contact();

  boolean contact(String contact);

  List<MetaType> meeting();

  boolean meeting(String meeting);
}
