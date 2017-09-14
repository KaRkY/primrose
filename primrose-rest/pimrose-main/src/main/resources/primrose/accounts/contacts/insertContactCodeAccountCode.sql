insert into t_account_contacts(
  account_contact_type_id,
  account_id,
  contact_id
) values (
  (select account_contact_type_id from t_account_contact_types where account_contact_type_code = :account_contact_type_code),
  (select account_id from t_accounts where account_code = :account_code),
  (select contact_id from t_contacts where contact_code = :contact_code)
)
