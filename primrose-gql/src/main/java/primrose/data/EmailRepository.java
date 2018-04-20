package primrose.data;

import java.util.List;
import java.util.Set;

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

   List<EmailFullDisplay> customerEmails(CodeId code);

   List<EmailFullDisplay> customerEmailsForUpdate(CodeId code);

   List<EmailFullDisplay> contactEmails(CodeId code);

   List<EmailFullDisplay> contactEmailsForUpdate(CodeId code);
}
