insert into t_account_addresses(
  account_address_type_id,
  account_id,
  address_id
) values (
  (select account_address_type_id from t_account_address_types where account_address_type_code = :account_address_type_code),
  :account_id,
  :address_id
)
