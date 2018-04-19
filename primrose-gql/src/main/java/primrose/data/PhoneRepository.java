package primrose.data;

import java.util.List;
import java.util.Set;

import primrose.service.CodeId;
import primrose.service.PhoneFullDisplay;

public interface PhoneRepository {

  long save(String phone);

  Long get(String phone);

  void assignToCustomer(CodeId code, long phoneId, String phoneType, Boolean primary);

  void assignToContact(CodeId code, long phoneId, String phoneType, Boolean primary);

  void removeFromCustomer(CodeId code, long phoneId);

  void removeExceptFromCustomer(CodeId code, Set<Long> phoneIds);

  void removeFromContact(CodeId code, long phoneId);

  void removeExceptFromContact(CodeId code, Set<Long> phoneIds);

  boolean isAssignedToCustomer(CodeId code, long phoneId);

  boolean isAssignedToContact(CodeId code, long phoneId);

  List<PhoneFullDisplay> customerPhones(CodeId code);

  List<PhoneFullDisplay> customerPhonesForUpdate(CodeId code);

  List<PhoneFullDisplay> contactPhones(CodeId code);

  List<PhoneFullDisplay> contactPhonesForUpdate(CodeId code);
}
