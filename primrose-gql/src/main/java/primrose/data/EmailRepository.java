package primrose.data;

import java.util.Set;

import com.google.common.collect.ImmutableList;

import primrose.service.CodeId;
import primrose.service.EmailFullDisplay;

public interface EmailRepository {

  long save(String email);

  Long get(String email);

  void assignToCustomer(CodeId code, long emailId, String emailType, Boolean primary);

  void assignToContact(CodeId code, long emailId, String emailType, Boolean primary);

  void removeFromCustomer(CodeId code, long emailId);

  void removeExceptFromCustomer(CodeId code, Set<Long> emailIds);

  void removeFromContact(CodeId code, long emailId);

  void removeExceptFromContact(CodeId code, Set<Long> emailIds);

  boolean isAssignedToCustomer(CodeId code, long emailId);

  boolean isAssignedToContact(CodeId code, long emailId);

  ImmutableList<EmailFullDisplay> customerEmails(CodeId code);

  ImmutableList<EmailFullDisplay> customerEmailsForUpdate(CodeId code);

  ImmutableList<EmailFullDisplay> contactEmails(CodeId code);

  ImmutableList<EmailFullDisplay> contactEmailsForUpdate(CodeId code);
}
