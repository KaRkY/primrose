insert into t_account_contacts(
  account_contact_type_id,
  account_id,
  contact_id
) values (
  (select account_contact_type_id from t_account_contact_types where account_contact_type_code = :account_contact_type_code),
  :account_id,
  :contact_id
)
