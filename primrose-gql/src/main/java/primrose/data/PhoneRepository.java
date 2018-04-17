package primrose.data;

import java.util.List;

import primrose.service.Phone;

public interface PhoneRepository {

  long save(String phone);

  void assignToCustomer(long customerId, long phoneId, String phoneType, Boolean primary);

  void assignToContact(long contactId, long phoneId, String phoneType, Boolean primary);

  List<Phone> customerPhones(long customerId);

  List<Phone> contactPhones(long contactId);
}
