package primrose.data;

import java.util.List;

import primrose.service.PhoneFullDisplay;

public interface PhoneRepository {

  long save(String phone);

  Long get(String phone);

  void assignToCustomer(long customerCodeId, long phoneId, String phoneType, Boolean primary);

  void assignToContact(long contactId, long phoneId, String phoneType, Boolean primary);

  void removeFromCustomer(long customerCodeId, long phoneId);

  void removeFromContact(long contactId, long phoneId);

  boolean isAssignedToCustomer(long customerCodeId, long phoneId);

  boolean isAssignedToContact(long contactId, long phoneId);

  List<PhoneFullDisplay> customerPhones(long customerCodeId);

  List<PhoneFullDisplay> customerPhonesForUpdate(long customerCodeId);

  List<PhoneFullDisplay> contactPhones(long contactId);

  List<PhoneFullDisplay> contactPhonesForUpdate(long contactId);
}
