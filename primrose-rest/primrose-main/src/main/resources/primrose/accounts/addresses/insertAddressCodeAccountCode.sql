insert into t_account_addresses(
  account_address_type_id,
  account_id,
  address_id
) values (
  (select account_address_type_id from t_account_address_types where account_address_type_code = :account_address_type_code),
  (select account_id from t_accounts where account_code = :account_code),
  (select address_id from t_addresses where address_code = :address_code)
)
