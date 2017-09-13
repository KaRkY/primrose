insert into t_accounts(
  account_id,
  account_type_id,
  display_name,
  full_name,
  email,
  phone,
  website,
  description,
  valid_from
) values (
  :account_id,
  (select account_type_id from t_account_types where account_type_code = :account_type_code),
  :display_name,
  :full_name,
  :email,
  :phone,
  :website,
  :description,
  current_timestamp
)