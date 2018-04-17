package primrose.data;

import java.util.List;

import primrose.service.Email;

public interface EmailRepository {

  long save(String email);

  void assignToCustomer(long customerId, long emailId, String emailType, Boolean primary);

  void assignToContact(long contactId, long emailId, String emailType, Boolean primary);

  List<Email> customerEmails(long customerId);

  List<Email> contactEmails(long contactId);
}
