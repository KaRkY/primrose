package primrose.data;

import java.util.List;

import primrose.service.EmailFullDisplay;

public interface EmailRepository {

  long save(String email);

  Long get(String email);

  void assignToCustomer(long customerCodeId, long emailId, String emailType, Boolean primary);

  void assignToContact(long contactId, long emailId, String emailType, Boolean primary);

  void removeFromCustomer(long customerCodeId, long emailId);

  void removeFromContact(long contactId, long emailId);

  boolean isAssignedToCustomer(long customerCodeId, long emailId);

  boolean isAssignedToContact(long contactId, long emailId);

  List<EmailFullDisplay> customerEmails(long customerCodeId);

  List<EmailFullDisplay> customerEmailsForUpdate(long customerCodeId);

  List<EmailFullDisplay> contactEmails(long contactId);

  List<EmailFullDisplay> contactEmailsForUpdate(long contactId);
}
